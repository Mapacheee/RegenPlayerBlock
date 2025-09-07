package me.mapacheee.temporalblocks.module;

import com.thewinterframework.module.annotation.ModuleComponent;
import com.thewinterframework.plugin.WinterPlugin;
import com.thewinterframework.plugin.module.PluginModule;
import me.mapacheee.temporalblocks.command.TemporalBlocksCommand;
import com.google.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;

@ModuleComponent
public class CommandModule implements PluginModule {

    private TemporalBlocksCommand temporalBlocksCommand;
    private JavaPlugin javaPlugin;

    @Inject
    public void setTemporalBlocksCommand(TemporalBlocksCommand temporalBlocksCommand) {
        this.temporalBlocksCommand = temporalBlocksCommand;
    }

    @Inject
    public void setPlugin(JavaPlugin plugin) {
        this.javaPlugin = plugin;
    }

    @Override
    public boolean onEnable(WinterPlugin plugin) {
        org.bukkit.plugin.java.JavaPlugin bukkitPlugin = (org.bukkit.plugin.java.JavaPlugin) javaPlugin;
        bukkitPlugin.getCommand("temporalblocks").setExecutor(temporalBlocksCommand);
        bukkitPlugin.getCommand("temporalblocks").setTabCompleter(temporalBlocksCommand);

        plugin.getSLF4JLogger().info("Comandos registrados correctamente");
        return true;
    }

    @Override
    public boolean onLoad(WinterPlugin plugin) {
        return true;
    }
    @Override
    public boolean onDisable(WinterPlugin plugin) {
        return true;
    }
}
