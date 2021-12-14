package me.minkuss.commands;

import me.minkuss.clan_plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class ClanKickCommand implements CommandExecutor {
    private clan_plugin _plugin;
    public ClanKickCommand(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = _plugin.getConfig();
        Player playersender = (Player) sender;
        String clan = config.getString("players." + playersender.getName() + ".clan");
        boolean inclan = config.getBoolean("players." + playersender.getName() + ".inclan?");
        String owning = config.getString("clans." + clan + ".owners");
        String playerkick = args[0];
        int mates_nubmer = config.getInt("clans." + clan + ".clanmates");

        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return false;
        }

        if (!inclan) {
            playersender.sendMessage(ChatColor.RED + "Вы не состоите в клане.");
            return false;
        }

        else if (!(owning.equals(playersender.getName()))) {
            playersender.sendMessage(ChatColor.RED + "Вы не являетесь владельцем клана.");
            return false;
        }

        else if (playerkick == null) {
            playersender.sendMessage(ChatColor.RED + "Введите имя игрока, которого хотите исключить.");
            return false;
        }

        else if (playersender.getName().equals(playerkick)) {
            playersender.sendMessage(ChatColor.RED + "Вы не можете исключить самого себя, используйте команду /leaveclan");
            return false;
        }

        List<String> playerslist = config.getStringList("clans." + clan + ".participants");
        playerslist.remove(playerkick);

        config.set("clans." + clan + ".participants", playerslist);
        config.set("players." + playerkick + ".clan", null);
        config.set("players." + playerkick + ".inclan?", false);
        config.set("clans." + clan + ".clanmates", mates_nubmer - 1);
        playersender.sendMessage(ChatColor.GOLD + "Вы успешно исключили игрока - " + playerkick + " из клана.");
        if (_plugin.getServer().getPlayer(playerkick) != null) {
            _plugin.getServer().getPlayer(playerkick).sendMessage(ChatColor.RED + "К сожалению, вас исключили из клана - " + clan + ". Зря они так поступили(");
        }
        else if (_plugin.getServer().getPlayer(playerkick) == null) {
            config.set("players." + playerkick + ".massage", "К сожалению, вас исключили из клана. Зря они так поступили(");
        }
        _plugin.saveConfig();


        return false;
    }
}
