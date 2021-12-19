package me.minkuss.commands;

import me.minkuss.clan_plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaveClanCommand implements CommandExecutor {
    private clan_plugin _plugin;
    public LeaveClanCommand(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = _plugin.getConfig();
        Player player = (Player) sender;
        String clan = config.getString("players." + player.getName() + ".clan");
        boolean inclan = config.getBoolean("players." + player.getName() + ".inclan?");
        int mates_nubmer = config.getInt("clans." + clan + ".clanmates");
        List<String> clanlist = config.getStringList("clanlist");

        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return false;
        }

        if (!inclan) {
            player.sendMessage(ChatColor.RED + "Вы не состоите в клане");
            return false;
        }

        else if (inclan) {
            if (mates_nubmer == 1) {
                clanlist.remove(clan);
                config.set("clanlist", clanlist);
                config.set("clans." + clan, null);
                config.set("players." + player.getName() + ".clan", null);
                config.set("players." + player.getName() + ".inclan?", false);
                player.sendMessage(ChatColor.GOLD + "Вы успешно вышли из клана. Теперь советуем вам скорее найти или создать новый!");
                _plugin.saveConfig();
                return false;
            }
            List<String> playerslist = config.getStringList("clans." + clan + ".participants");
            playerslist.remove(player.getName());

            config.set("clans." + clan + ".participants", playerslist);
            config.set("players." + player.getName() + ".clan", null);
            config.set("players." + player.getName() + ".inclan?", false);
            config.set("clans." + clan + ".clanmates", mates_nubmer - 1);
            player.sendMessage(ChatColor.GOLD + "Вы успешно вышли из клана. Теперь советуем вам скорее найти или создать новый!");
            _plugin.saveConfig();
        }

        return false;
    }
}
