package me.mapacheee.temporalblocks.module;

import com.example.temporalblocks.command.TemporalBlocksCommand;
import com.google.inject.Inject;
import com.thewinterframework.winter.WinterPlugin;
import com.thewinterframework.winter.annotation.ModuleComponent;
import com.thewinterframework.winter.module.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;

@ModuleComponent
public class CommandModule implements PluginModule {

    private TemporalBlocksCommand temporalBlocksCommand;

    @Inject
    public void setTemporalBlocksCommand(TemporalBlocksCommand temporalBlocksCommand) {
        this.temporalBlocksCommand = temporalBlocksCommand;
    }

    @Override
    public boolean onEnable(WinterPlugin plugin) {
        JavaPlugin javaPlugin = (JavaPlugin) plugin.getPlugin();
        javaPlugin.getCommand("temporalblocks").setExecutor(temporalBlocksCommand);
        javaPlugin.getCommand("temporalblocks").setTabCompleter(temporalBlocksCommand);

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
