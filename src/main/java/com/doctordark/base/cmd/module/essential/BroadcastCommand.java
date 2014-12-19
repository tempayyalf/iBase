package com.doctordark.base.cmd.module.essential;

import com.doctordark.base.cmd.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

/**
 * Command used for broadcasting messages.
 */
public class BroadcastCommand extends BaseCommand {

    private final String format;

    public BroadcastCommand(JavaPlugin plugin) {
        super("broadcast", "Broadcasts a messaging to the server.", "base.command.broadcast");
        this.setAliases(new String[]{});
        this.setUsage("/(command) <message>");

        FileConfiguration config = plugin.getConfig();
        this.format = config.getString("broadcast.format");
        // Causes it just to show default value: "&7%s");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage());
            return true;
        }

        StringBuilder message = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) {
            message.append(" ").append(args[i]);
        }

        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Locale.ENGLISH, format, message.toString())));
        return true;
    }
}