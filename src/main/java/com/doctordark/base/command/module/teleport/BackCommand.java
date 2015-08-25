package com.doctordark.base.command.module.teleport;

import com.doctordark.base.BaseConstants;
import com.doctordark.base.BasePlugin;
import com.doctordark.base.command.BaseCommand;
import com.doctordark.base.user.BaseUser;
import com.doctordark.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Collections;
import java.util.List;

public class BackCommand extends BaseCommand implements Listener {

    private final BasePlugin plugin;

    public BackCommand(BasePlugin plugin) {
        super("back", "Go to a players last known location.");
        setUsage("/(command) [playerName]");
        Bukkit.getPluginManager().registerEvents(this, this.plugin = plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }

        final Player target;
        if (args.length > 0 && sender.hasPermission(command.getPermission() + ".others")) {
            target = BukkitUtils.playerWithNameOrUUID(args[0]);
            if (target == null) {
                sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                return true;
            }
        } else {
            target = (Player) sender;
        }

        BaseUser targetUser = plugin.getUserManager().getUser(target.getUniqueId());
        Location previous = targetUser.getBackLocation();

        if (previous == null) {
            sender.sendMessage(ChatColor.RED + target.getName() + " doesn't have a back location.");
            return true;
        }

        ((Player) sender).teleport(previous);

        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Teleported to back location of " + target.getName() + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        plugin.getUserManager().getUser(player.getUniqueId()).setBackLocation(player.getLocation());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        PlayerTeleportEvent.TeleportCause cause = event.getCause();
        if (cause == PlayerTeleportEvent.TeleportCause.COMMAND || cause == PlayerTeleportEvent.TeleportCause.UNKNOWN || cause == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            plugin.getUserManager().getUser(event.getPlayer().getUniqueId()).setBackLocation(event.getFrom().clone());
        }
    }
}
