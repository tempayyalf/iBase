package com.doctordark.base.cmd;

import com.doctordark.base.BasePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the command manager.
 */
public class SimpleCommandManager implements CommandManager {

    private static final String PERMISSION_MESSAGE = "No permission for this command!";

    private final Map<String, BaseCommand> commandMap;

    public SimpleCommandManager(final BasePlugin plugin) {
        final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        commandMap = new HashMap<String, BaseCommand>();

        // Load all the modules first.
        Bukkit.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable() {
            @Override
            public void run() {
            for (BaseCommand command : commandMap.values()) {
                String cmdName = command.getName();
                PluginCommand pluginCommand = plugin.getCommand(cmdName);

                if (pluginCommand == null) {
                    console.sendMessage(ChatColor.RED + "Failed to register command " + cmdName +
                            " as it is not added in the plugin.yml!");
                    continue;
                }

                pluginCommand.setExecutor(command);
                pluginCommand.setDescription(command.getDescription());
                pluginCommand.setUsage(command.getUsage());
                pluginCommand.setPermission(command.getPermission());
                pluginCommand.setPermissionMessage(PERMISSION_MESSAGE);
                pluginCommand.setAliases(Arrays.asList(command.getAliases()));
            }
            }
        }, 1L);
    }

    @Override
    public boolean isCommand(BaseCommand command) {
        return commandMap.containsValue(command);
    }

    @Override
    public void registerAll(BaseCommandModule module) {
        if (module.isEnabled()) {
            Set<BaseCommand> commands = module.getCommands();
            for (BaseCommand command : commands) {
                commandMap.put(command.getName(), command);
            }
        }
    }

    @Override
    public void registerCommand(BaseCommand command) {
        commandMap.put(command.getName(), command);
    }

    @Override
    public void registerCommands(BaseCommand[] commands) {
        for (BaseCommand command : commands) {
            commandMap.put(command.getName(), command);
        }
    }

    @Override
    public void unregisterCommand(BaseCommand command) {
        commandMap.values().remove(command);
    }

    @Override
    public BaseCommand getCommand(String id) {
        return commandMap.containsKey(id) ? commandMap.get(id) : null;
    }
}
