package com.doctordark.base.listener;

import com.doctordark.base.BasePlugin;
import com.doctordark.base.user.BaseUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinListener implements Listener {

    private static final int MAX_ACCOUNTS_PER_IP = 3; //TODO:
    private final BasePlugin plugin;

    public JoinListener(BasePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        BaseUser baseUser = plugin.getUserManager().getUser(uuid);
        baseUser.tryLoggingName(player);
        baseUser.tryLoggingAddress(player.getAddress().getAddress().getHostName());
    }
}
