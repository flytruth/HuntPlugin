package com.daniel.huntplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HuntCommand implements CommandExecutor {

    private final HuntPlugin plugin;

    public HuntCommand(HuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("hunt.admin")) {
            sender.sendMessage(ChatColor.RED + "У вас нет прав.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.GOLD + "=== HuntPlugin Help ===");
            sender.sendMessage(ChatColor.YELLOW + "/hunt start <ник>" + ChatColor.GRAY + " - начать охоту на игрока");
            sender.sendMessage(ChatColor.YELLOW + "/hunt stop" + ChatColor.GRAY + " - остановить текущую охоту");
            sender.sendMessage(ChatColor.YELLOW + "/hunt help" + ChatColor.GRAY + " - показать это сообщение");
            sender.sendMessage(ChatColor.DARK_GRAY + "Автор: " + ChatColor.AQUA + "DevFly (BexindStudio)");
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Укажите ник: /hunt start <ник>");
                return true;
            }

            if (plugin.huntedPlayer != null) {
                sender.sendMessage(ChatColor.RED + "Охота уже идет. Сначала остановите ее.");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Игрок не найден или не в сети.");
                return true;
            }

            target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, PotionEffect.INFINITE_DURATION, 0, false, false));

            sender.sendMessage(ChatColor.GREEN + "Охота началась на " + target.getName());
            plugin.startHunt(target.getUniqueId());

        } else if (args[0].equalsIgnoreCase("stop")) {
            if (plugin.huntedPlayer == null) {
                sender.sendMessage(ChatColor.RED + "Сейчас нет активной охоты.");
                return true;
            }
            plugin.stopHunt(null);
            sender.sendMessage(ChatColor.GREEN + "Охота остановлена вручную.");
        } else {
            sender.sendMessage(ChatColor.RED + "Неизвестная команда. Введите /hunt help");
        }

        return true;
    }
}
