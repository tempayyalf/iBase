package com.doctordark.base.kit.argument;

import com.doctordark.base.BasePlugin;
import com.doctordark.base.kit.Kit;
import com.doctordark.util.command.CommandArgument;
import com.google.common.primitives.Ints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An {@link CommandArgument} used to set the
 * position of a {@link Kit} in the GUI.
 */
public class KitSetIndexArgument extends CommandArgument {

    private final BasePlugin plugin;

    public KitSetIndexArgument(BasePlugin plugin) {
        super("setindex", "Sets the position of a kit for the GUI");
        this.plugin = plugin;
        this.aliases = new String[]{"setorder", "setindex", "setpos", "setposition"};
        this.permission = "hcf.command.kit.argument." + getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <kitName> <index[0 = minimum]>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return true;
        }

        Kit kit = plugin.getKitManager().getKit(args[1]);

        if (kit == null) {
            sender.sendMessage(ChatColor.RED + "Kit '" + args[1] + "' not found.");
            return true;
        }

        Integer newIndex = Ints.tryParse(args[2]);

        if (newIndex == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
            return true;
        }

        if (newIndex < 0) {
            sender.sendMessage(ChatColor.RED + "The kit index must be a minimum of " + 0 + '.');
            return true;
        }

        List<Kit> kits = plugin.getKitManager().getKits();
        int totalKitAmount = kits.size();

        if (newIndex > totalKitAmount) {
            sender.sendMessage(ChatColor.RED + "The kit index must be a maximum of " + totalKitAmount + '.');
            return true;
        }

        int previousIndex = kits.indexOf(kit);

        if (newIndex == previousIndex) {
            sender.sendMessage(ChatColor.RED + "Index of kit " + kit.getName() + " is already " + newIndex + '.');
            return true;
        }

        kits.remove(kit);
        kits.add(newIndex, kit);

        sender.sendMessage(ChatColor.AQUA + "Set the index of kit " + args[1] + " from " + previousIndex + " to " + newIndex + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }

        return plugin.getKitManager().getKits().stream().map(Kit::getName).collect(Collectors.toList());
    }
}