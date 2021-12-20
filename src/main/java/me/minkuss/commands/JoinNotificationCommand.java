package me.minkuss.commands;

import me.minkuss.clan_plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinNotificationCommand implements CommandExecutor {
    private clan_plugin _plugin;
    public JoinNotificationCommand(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return false;
        }

        FileConfiguration config = _plugin.getConfig();
        Player player = (Player) sender;
        List<String> joins = config.getStringList("players." + player.getName() + ".joiners");

        if (joins == null) {
            player.sendMessage(ChatColor.GOLD + "У вас нет активных запросов");
            return false;
        }

        player.sendMessage(ChatColor.BLUE + "[Список запросов]: ");
        for (String item : joins) {
            player.sendMessage(ChatColor.GOLD + item);
        }

        return false;
    }
}
