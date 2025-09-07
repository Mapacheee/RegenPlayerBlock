package me.mapacheee.temporalblocks.service;

import com.example.temporalblocks.config.ConfigService;
import com.example.temporalblocks.data.ExplosionData;
import com.google.inject.Inject;
import com.thewinterframework.winter.annotation.OnDisable;
import com.thewinterframework.winter.annotation.Service;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExplosionService {

    private final Plugin plugin;
    private final Logger logger;
    private final ConfigService configService;
    private final ConcurrentHashMap<Integer, ExplosionData> explosionData;

    @Inject
    public ExplosionService(Plugin plugin, Logger logger, ConfigService configService) {
        this.plugin = plugin;
        this.logger = logger;
        this.configService = configService;
        this.explosionData = new ConcurrentHashMap<>();
    }

    @OnDisable
    public void onDisable() {
        explosionData.clear();
    }

    public void registerExplosion(Location location, List<Block> destroyedBlocks) {
        if (!configService.isExplosionRegenerationEnabled()) {
            return;
        }

        String worldName = location.getWorld().getName();
        if (!configService.getEnabledWorlds().contains(worldName)) {
            return;
        }

        List<Block> validBlocks = new ArrayList<>();
        for (Block block : destroyedBlocks) {
            // Solo regenerar bloques que no sean aire
            if (!block.getType().isAir()) {
                validBlocks.add(block);
            }
        }

        if (validBlocks.isEmpty()) {
            return;
        }

        int explosionId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        ExplosionData data = new ExplosionData(location, validBlocks);
        explosionData.put(explosionId, data);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            regenerateExplosion(explosionId);
        }, configService.getBlockDurationSeconds() * 20L); // 20 ticks = 1 segundo

        if (configService.isDebugEnabled()) {
            logger.info("Explosión registrada para regeneración: {} bloques en {}", 
                validBlocks.size(), location.toString());
        }
    }

    private void regenerateExplosion(int explosionId) {
        ExplosionData data = explosionData.remove(explosionId);
        if (data == null) {
            return;
        }

        int regeneratedCount = 0;
        for (Block originalBlock : data.getDestroyedBlocks()) {
            Block currentBlock = originalBlock.getLocation().getBlock();
            if (currentBlock.getType().isAir()) {
                currentBlock.setType(originalBlock.getType());
                currentBlock.setBlockData(originalBlock.getBlockData());
                regeneratedCount++;
            }
        }

        if (configService.isDebugEnabled() && regeneratedCount > 0) {
            logger.info("Regenerados {} bloques de explosión en {}", 
                regeneratedCount, data.getExplosionLocation().toString());
        }
    }

    public int getPendingExplosions() {
        return explosionData.size();
    }
}
