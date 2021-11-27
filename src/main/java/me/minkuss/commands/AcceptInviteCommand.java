package me.minkuss.commands;

import me.minkuss.clan_plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class AcceptInviteCommand implements CommandExecutor {
    private clan_plugin _plugin;
    public AcceptInviteCommand(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = _plugin.getConfig();
        Player player = (Player) sender;
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return false;
        }
        boolean accepting = config.getBoolean("players." + player.getName() + ".accepting");
        String clanName = config.getString("players." + player.getName() + ".acceptingClan");

        if (accepting) {
            int mates_nubmer = config.getInt("clans." + clanName + ".clanmates");
            List<String> clanplayers = config.getStringList("clans." + clanName + ".participants");
            String inviter = config.getString("players." + player.getName() + ".inviter");

            config.set("players." + player.getName() + ".inclan?", true);
            config.set("players." + player.getName() + ".clan", clanName);

            clanplayers.add(player.getName());
            config.set("clans." + clanName + ".participants", clanplayers);
            config.set("clans." + clanName + ".clanmates", mates_nubmer + 1);

            config.set("players." + player.getName() + ".accepting", null);
            config.set("players." + player.getName() + ".acceptingClan", null);
            config.set("players." + player.getName() + ".inviter", null);
            _plugin.getServer().getPlayer(inviter).sendMessage(ChatColor.GOLD + "Игрок - " + player.getName() + " принял ваше приглашение.");
            _plugin.saveConfig();
            player.sendMessage(ChatColor.GOLD + "Теперь вы в клане - " + clanName);
        }

        return false;
    }
}
