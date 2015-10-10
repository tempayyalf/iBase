package com.doctordark.base.command.module.essential;

import com.doctordark.base.BaseConstants;
import com.doctordark.base.BasePlugin;
import com.doctordark.base.command.BaseCommand;
import com.doctordark.base.StaffPriority;
import com.doctordark.base.user.BaseUser;
import com.doctordark.util.BukkitUtils;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WhoisCommand extends BaseCommand {

    private static final Map<Integer, String> CLIENT_PROTOCOL_IDS = ImmutableMap.of(
            4, "1.7.2 -> 1.7.5",
            5, "1.7.6 -> 1.7.10",
            47, "1.8 -> 1.8.8"
    );

    private final BasePlugin plugin;

    public WhoisCommand(BasePlugin plugin) {
        super("whois", "Check information about a player.");
        this.plugin = plugin;
        this.setUsage("/(command) [playerName]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage());
            return true;
        }

        Player target = BukkitUtils.playerWithNameOrUUID(args[0]);

        if (target == null || !canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }

        Location location = target.getLocation();
        World world = location.getWorld();

        BaseUser baseUser = plugin.getUserManager().getUser(target.getUniqueId());
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.GREEN + " [" + target.getDisplayName() + ChatColor.GREEN + ']');
        sender.sendMessage(ChatColor.YELLOW + "  Health: " + ChatColor.GOLD + target.getHealth() + '/' + target.getMaxHealth());
        sender.sendMessage(ChatColor.YELLOW + "  Hunger: " + ChatColor.GOLD + target.getFoodLevel() + '/' + 20 + " (" + target.getSaturation() + " saturation)");
        sender.sendMessage(ChatColor.YELLOW + "  Exp/Level: " + ChatColor.GOLD + target.getExp() + '/' + target.getLevel());
        sender.sendMessage(ChatColor.YELLOW + "  Location: " + ChatColor.GOLD + world.getName() + ' ' +
                ChatColor.GRAY + '[' + WordUtils.capitalizeFully(world.getEnvironment().name().replace('_', ' ')) + "] " +
                ChatColor.GOLD + '(' + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ')');
        sender.sendMessage(ChatColor.YELLOW + "  Vanished: " + ChatColor.GOLD + baseUser.isVanished() + " (priority=" + StaffPriority.of(target).getPriorityLevel() + ')');
        sender.sendMessage(ChatColor.YELLOW + "  Staff Chat: " + ChatColor.GOLD + baseUser.isInStaffChat());
        sender.sendMessage(ChatColor.YELLOW + "  Operator: " + ChatColor.GOLD + target.isOp());
        sender.sendMessage(ChatColor.YELLOW + "  Game Mode: " + ChatColor.GOLD + WordUtils.capitalizeFully(target.getGameMode().name().replace('_', ' ')));
        sender.sendMessage(ChatColor.YELLOW + "  Idle Time: " + ChatColor.GOLD + DurationFormatUtils.formatDurationWords(BukkitUtils.getIdleTime(target), true, true));
        sender.sendMessage(ChatColor.YELLOW + "  IP Address: " + ChatColor.GOLD + target.getAddress().getHostString());

        int version = ((CraftPlayer) target).getHandle().playerConnection.networkManager.getVersion();
        sender.sendMessage(ChatColor.YELLOW + "  Client Version: " + ChatColor.GOLD + version +
                ChatColor.GRAY + " [" + MoreObjects.firstNonNull(CLIENT_PROTOCOL_IDS.get(version), "Unknown (check at http://wiki.vg/Protocol_version_numbers)") + "]");

        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.<String>emptyList();
    }
}