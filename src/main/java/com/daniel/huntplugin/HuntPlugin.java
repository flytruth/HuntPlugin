package com.daniel.huntplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class HuntPlugin extends JavaPlugin {

    public UUID huntedPlayer;
    public int huntTaskId = -1;

    public int huntDuration;
    public int broadcastInterval;
    public int xpReward;

    public int secondsLeft;
    public int secondsToBroadcast;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        huntDuration = getConfig().getInt("hunt-duration", 25);
        broadcastInterval = getConfig().getInt("broadcast-interval", 30);
        xpReward = getConfig().getInt("xp-reward", 75);

        getCommand("hunt").setExecutor(new HuntCommand(this));
        getServer().getPluginManager().registerEvents(new HuntListener(this), this);

        getLogger().info("HuntPlugin by DevFly (BexindStudio) включен!");
    }

    @Override
    public void onDisable() {
        if (huntedPlayer != null) {
            stopHunt(null);
        }
    }

    public void startHunt(UUID targetId) {
        this.huntedPlayer = targetId;
        Player target = Bukkit.getPlayer(targetId);
        if (target == null) return;

        Bukkit.broadcastMessage(ChatColor.RED + "Началась охота на игрока " + target.getName() + "!");

        secondsLeft = huntDuration * 60;
        secondsToBroadcast = broadcastInterval;

        huntTaskId = Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (huntedPlayer == null || Bukkit.getPlayer(huntedPlayer) == null) {
                stopHunt(null);
                return;
            }

            if (secondsLeft <= 0) {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Время охоты вышло! Игрок выжил.");
                stopHunt(null);
                return;
            }

            updateTabList();

            if (secondsToBroadcast <= 0) {
                Player p = Bukkit.getPlayer(huntedPlayer);
                String worldName = p.getWorld().getName();
                int x = p.getLocation().getBlockX();
                int y = p.getLocation().getBlockY();
                int z = p.getLocation().getBlockZ();

                Bukkit.broadcastMessage(ChatColor.GOLD + "[ОХОТА] " + ChatColor.WHITE + p.getName() +
                        " находится в мире " + worldName + " на координатах: X:" + x + " Y:" + y + " Z:" + z);

                secondsToBroadcast = broadcastInterval;
            }

            secondsLeft--;
            secondsToBroadcast--;

        }, 0L, 20L).getTaskId();
    }

    public void updateTabList() {
        if (huntedPlayer == null) return;
        Player target = Bukkit.getPlayer(huntedPlayer);
        if (target == null) return;

        String worldName = target.getWorld().getName();
        int x = target.getLocation().getBlockX();
        int y = target.getLocation().getBlockY();
        int z = target.getLocation().getBlockZ();

        String timer = String.format("%02d:%02d", secondsLeft / 60, secondsLeft % 60);

        String header = ChatColor.GOLD + "" + ChatColor.BOLD + "〔 ОХОТА 〕 " + ChatColor.RESET +
                ChatColor.YELLOW + target.getName() + "\n" +
                ChatColor.WHITE + "Координаты: " + ChatColor.GRAY + "X:" + x + " Y:" + y + " Z:" + z + " (" + worldName + ")\n" +
                ChatColor.WHITE + "Обновление через: " + ChatColor.RED + secondsToBroadcast + "с " +
                ChatColor.WHITE + "| Таймер: " + ChatColor.RED + timer;

        String footer = ChatColor.DARK_GRAY + "Плагин создан: " + ChatColor.AQUA + "DevFly / BexindStudio";

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.setPlayerListHeaderFooter(header, footer);
        }
    }

    public void stopHunt(UUID killerId) {
        if (huntTaskId != -1) {
            Bukkit.getScheduler().cancelTask(huntTaskId);
            huntTaskId = -1;
        }

        if (huntedPlayer != null) {
            Player target = Bukkit.getPlayer(huntedPlayer);
            if (target != null && target.isOnline()) {
                target.removePotionEffect(org.bukkit.potion.PotionEffectType.GLOWING);
            }
        }

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.setPlayerListHeaderFooter("", "");
        }

        if (killerId != null) {
            Player killer = Bukkit.getPlayer(killerId);
            if (killer != null) {
                killer.giveExp(xpReward);
                Bukkit.broadcastMessage(ChatColor.GREEN + "Охота окончена! " + killer.getName() + " убил цель и получил " + xpReward + " уровней опыта.");
            }
        } else if (huntedPlayer != null) {
            Bukkit.broadcastMessage(ChatColor.GRAY + "Охота остановлена.");
        }

        huntedPlayer = null;
    }
}
