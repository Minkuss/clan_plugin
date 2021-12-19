package me.minkuss.commands;

import me.minkuss.clan_plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class JoinClanCommand implements CommandExecutor {
    private clan_plugin _plugin;
    public JoinClanCommand(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return false;
        }

        Player player = (Player) sender;
        FileConfiguration config = _plugin.getConfig();
        String clan = args[0];
        List<String> clanlist = config.getStringList("clanlist");
        String owner = config.getString("clans." + clan + ".owners");
        List<String> joins = config.getStringList("players." + owner + ".joiners");
        boolean inclan = config.getBoolean("players." + player.getName() + ".inclan?");

        if (clan == null) {
            player.sendMessage(ChatColor.RED + "Введите название клана");
            return false;
        }

        if (!(clanlist.contains(clan))) {
            player.sendMessage(ChatColor.RED + "Такого клана не существует");
            return false;
        }

        if (inclan) {
            player.sendMessage(ChatColor.RED + "Вы уже состоите в клане");
            return false;
        }

        if (joins != null) {
            if (joins.contains(player.getName())) {
                player.sendMessage(ChatColor.RED + "Вы уже отправили приглашение в этот клан");
                return false;
            }
            joins.add(player.getName());
            config.set("players." + owner + ".joiners", joins);
            player.sendMessage(ChatColor.GOLD + "Вы успешно отправили приглашение");
            _plugin.saveConfig();
            return false;
        }

        else if (joins == null) {
            List<String> joinz = List.of(player.getName());
            config.set("players." + owner + ".joiners", joinz);
            player.sendMessage(ChatColor.GOLD + "Вы успешно отправили приглашение");
            _plugin.saveConfig();
            return false;
        }

        return false;
    }
}
