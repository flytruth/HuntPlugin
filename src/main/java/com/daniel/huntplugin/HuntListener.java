package com.daniel.huntplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class HuntListener implements Listener {

    private final HuntPlugin plugin;

    public HuntListener(HuntPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();

        if (plugin.huntedPlayer != null && dead.getUniqueId().equals(plugin.huntedPlayer)) {
            Player killer = dead.getKiller();

            if (killer != null) {
                plugin.stopHunt(killer.getUniqueId());
            } else {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Цель умерла без чужого вмешательства. Охота окончена.");
                plugin.stopHunt(null);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player quitter = event.getPlayer();

        if (plugin.huntedPlayer != null && quitter.getUniqueId().equals(plugin.huntedPlayer)) {
            Location loc = quitter.getLocation();
            for (ItemStack item : quitter.getInventory().getContents()) {
                if (item != null) {
                    loc.getWorld().dropItemNaturally(loc, item);
                }
            }
            quitter.getInventory().clear();
            quitter.setHealth(0.0);

            Bukkit.broadcastMessage(ChatColor.RED + quitter.getName() + " попытался сбежать с сервера во время охоты и был казнен! Лут выпаден.");
            plugin.stopHunt(null);
        }
    }
}
