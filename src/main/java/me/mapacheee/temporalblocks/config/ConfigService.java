package me.mapacheee.temporalblocks.config;

import com.google.inject.Inject;
import com.thewinterframework.winter.annotation.Service;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Service
public class ConfigService {

    private final Plugin plugin;
    private final Logger logger;
    private YamlConfiguration config;
    private File configFile;

    @Inject
    public ConfigService(Plugin plugin, Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public void loadConfig() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            if (!configFile.exists()) {
                try (InputStream inputStream = plugin.getResource("config.yml")) {
                    if (inputStream != null) {
                        Files.copy(inputStream, configFile.toPath());
                        logger.info("Archivo config.yml creado correctamente");
                    }
                }
            }

            config = YamlConfiguration.loadConfiguration(configFile);
            logger.info("Configuración cargada exitosamente");
        } catch (IOException e) {
            logger.error("Error al cargar la configuración", e);
        }
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            logger.error("Error al guardar la configuración", e);
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        logger.info("Configuración recargada");
    }

    public List<String> getEnabledWorlds() {
        return config.getStringList("enabled-worlds");
    }

    public int getBlockDurationSeconds() {
        return config.getInt("block-duration-seconds", 3);
    }

    public boolean isExplosionRegenerationEnabled() {
        return config.getBoolean("regenerate-explosions", true);
    }

    public boolean isDebugEnabled() {
        return config.getBoolean("debug", false);
    }

    public List<String> getExcludedBlocks() {
        return config.getStringList("excluded-blocks");
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
