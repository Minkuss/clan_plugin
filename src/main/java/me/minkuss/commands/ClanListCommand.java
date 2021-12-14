package me.minkuss.commands;

import me.minkuss.clan_plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class ClanListCommand implements CommandExecutor {
    private clan_plugin _plugin;
    public ClanListCommand(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = _plugin.getConfig();
        List<String> clanlist = config.getStringList("clanlist");

        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может ввести только игрок.");
            return false;
        }

        for (String item : clanlist) {
            sender.sendMessage(ChatColor.GOLD + item);
        }

        return false;
    }
}
