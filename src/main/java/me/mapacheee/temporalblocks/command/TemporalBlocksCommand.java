package me.mapacheee.temporalblocks.command;

import com.example.temporalblocks.config.ConfigService;
import com.example.temporalblocks.service.ExplosionService;
import com.example.temporalblocks.service.TemporalBlockService;
import com.google.inject.Inject;
import com.thewinterframework.winter.annotation.ListenerComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@ListenerComponent
public class TemporalBlocksCommand implements CommandExecutor, TabCompleter {

    private final ConfigService configService;
    private final TemporalBlockService temporalBlockService;
    private final ExplosionService explosionService;

    @Inject
    public TemporalBlocksCommand(ConfigService configService, 
                               TemporalBlockService temporalBlockService,
                               ExplosionService explosionService) {
        this.configService = configService;
        this.temporalBlockService = temporalBlockService;
        this.explosionService = explosionService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, 
                           @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("temporalblocks.admin")) {
            sender.sendMessage(ChatColor.RED + "No tienes permisos para usar este comando.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                configService.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Configuración recargada exitosamente.");
                break;

            case "info":
                sendInfo(sender);
                break;

            case "clear":
                temporalBlockService.clearAllTemporalBlocks();
                sender.sendMessage(ChatColor.GREEN + "Todos los bloques temporales han sido limpiados.");
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== TemporalBlocks ===");
        sender.sendMessage(ChatColor.YELLOW + "/temporalblocks reload " + ChatColor.WHITE + "- Recarga la configuración");
        sender.sendMessage(ChatColor.YELLOW + "/temporalblocks info " + ChatColor.WHITE + "- Muestra información del plugin");
        sender.sendMessage(ChatColor.YELLOW + "/temporalblocks clear " + ChatColor.WHITE + "- Limpia todos los bloques temporales");
    }

    private void sendInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Información de TemporalBlocks ===");
        sender.sendMessage(ChatColor.YELLOW + "Bloques temporales activos: " + 
            ChatColor.WHITE + temporalBlockService.getActiveBlockCount());
        sender.sendMessage(ChatColor.YELLOW + "Explosiones pendientes: " + 
            ChatColor.WHITE + explosionService.getPendingExplosions());
        sender.sendMessage(ChatColor.YELLOW + "Mundos habilitados: " + 
            ChatColor.WHITE + String.join(", ", configService.getEnabledWorlds()));
        sender.sendMessage(ChatColor.YELLOW + "Duración de bloques: " + 
            ChatColor.WHITE + configService.getBlockDurationSeconds() + " segundos");
        sender.sendMessage(ChatColor.YELLOW + "Regeneración de explosiones: " + 
            ChatColor.WHITE + (configService.isExplosionRegenerationEnabled() ? "Activada" : "Desactivada"));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, 
                                               @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) {
            return Arrays.asList("reload", "info", "clear");
        }

        return null;
    }
}
