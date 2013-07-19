package com.endercreators.perms;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {

    EnderPerms plugin;

    public PlayerListener(EnderPerms plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
    }

    // Quit
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {

    }

    // Respawning
    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent event) {

    }

    // Death
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {

    }


}