package me.minkuss.commands;

import me.minkuss.clan_plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class ClanInfoCommand implements CommandExecutor {
    private clan_plugin _plugin;
    public ClanInfoCommand(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return false;
        }

        Player player =  (Player) sender;
        FileConfiguration config = _plugin.getConfig();
        String clanName = args[0];
        List<String> clanlist = config.getStringList("clanlist");
        String owner = config.getString("clans." + clanName + ".owners");
        int clanmates = config.getInt("clans." + clanName + ".clanmates");
        List<String> participants = config.getStringList("clans." + clanName + ".participants");

        if (args[0] == null) {
            player.sendMessage(ChatColor.RED + "Введите название клана");
            return false;
        }

        if (!(clanlist.contains(clanName))) {
            player.sendMessage(ChatColor.RED + "Такого клана не существует");
            return false;
        }

        player.sendMessage(ChatColor.BLUE + "[Информация]");
        player.sendMessage(ChatColor.GREEN + "[Название клана]: " + ChatColor.GOLD + clanName);
        player.sendMessage(ChatColor.GREEN + "[Владелец клана]: " + ChatColor.GOLD + owner);
        player.sendMessage(ChatColor.GREEN + "[Количество игроков]: " + ChatColor.GOLD + clanmates);
        player.sendMessage(ChatColor.GREEN + "[Список игроков]: ");

        for (String item : participants) {
            player.sendMessage(ChatColor.GOLD + item);
        }

        return false;
    }
}
