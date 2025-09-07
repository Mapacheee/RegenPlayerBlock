package me.mapacheee.temporalblocks.service;

import com.example.temporalblocks.config.ConfigService;
import com.example.temporalblocks.data.TemporalBlock;
import com.google.inject.Inject;
import com.thewinterframework.winter.annotation.OnEnable;
import com.thewinterframework.winter.annotation.OnDisable;
import com.thewinterframework.winter.annotation.RepeatingTask;
import com.thewinterframework.winter.annotation.Service;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class TemporalBlockService {

    private final Plugin plugin;
    private final Logger logger;
    private final ConfigService configService;
    private final ConcurrentHashMap<Location, TemporalBlock> temporalBlocks;

    @Inject
    public TemporalBlockService(Plugin plugin, Logger logger, ConfigService configService) {
        this.plugin = plugin;
        this.logger = logger;
        this.configService = configService;
        this.temporalBlocks = new ConcurrentHashMap<>();
    }

    @OnEnable
    public void onEnable() {
        configService.loadConfig();
        logger.info("TemporalBlockService iniciado correctamente");
    }

    @OnDisable
    public void onDisable() {
        temporalBlocks.clear();
        logger.info("TemporalBlockService desactivado");
    }


    public void addTemporalBlock(Block block, Player player) {
        String worldName = block.getWorld().getName();

        if (!configService.getEnabledWorlds().contains(worldName)) {
            return;
        }

        if (player.hasPermission("temporalblocks.bypass")) {
            if (configService.isDebugEnabled()) {
                logger.info("Jugador {} tiene permisos de bypass", player.getName());
            }
            return;
        }

        String blockType = block.getType().name();
        if (configService.getExcludedBlocks().contains(blockType)) {
            if (configService.isDebugEnabled()) {
                logger.info("Bloque {} está excluido de la eliminación temporal", blockType);
            }
            return;
        }

        Location location = block.getLocation();
        TemporalBlock temporalBlock = new TemporalBlock(
            location, 
            block.getType(), 
            block.getBlockData().clone(),
            player.getName(),
            System.currentTimeMillis() + (configService.getBlockDurationSeconds() * 1000L)
        );

        temporalBlocks.put(location, temporalBlock);

        if (configService.isDebugEnabled()) {
            logger.info("Bloque temporal registrado en {} por jugador {}", 
                location.toString(), player.getName());
        }
    }

    @RepeatingTask(every = 1, unit = TimeUnit.SECONDS)
    public void checkExpiredBlocks() {
        if (temporalBlocks.isEmpty()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        Iterator<TemporalBlock> iterator = temporalBlocks.values().iterator();

        while (iterator.hasNext()) {
            TemporalBlock temporalBlock = iterator.next();

            if (currentTime >= temporalBlock.getExpireTime()) {
                // Programar la eliminación del bloque en el hilo principal
                Bukkit.getScheduler().runTask(plugin, () -> {
                    Block block = temporalBlock.getLocation().getBlock();
                    if (block.getType() == temporalBlock.getOriginalMaterial()) {
                        block.setType(Material.AIR);

                        if (configService.isDebugEnabled()) {
                            logger.info("Bloque temporal eliminado en {}", 
                                temporalBlock.getLocation().toString());
                        }
                    }
                });

                iterator.remove();
            }
        }
    }


    public int getActiveBlockCount() {
        return temporalBlocks.size();
    }
    public void clearAllTemporalBlocks() {
        temporalBlocks.clear();
    }
    public boolean isTemporalBlock(Location location) {
        return temporalBlocks.containsKey(location);
    }
}
