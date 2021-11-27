package me.minkuss.commands;

import me.minkuss.clan_plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class DeleteClanCommand implements CommandExecutor {
    private clan_plugin _plugin;
    public DeleteClanCommand(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = _plugin.getConfig();
        Player player = (Player) sender;
        String clan = config.getString("players." + player.getName() + ".clan");
        boolean inclan = config.getBoolean("players." + player.getName() + ".inclan?");
        String owning = config.getString("clans." + clan + ".owners");
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return false;
        }

        if (!inclan) {
            player.sendMessage(ChatColor.RED + "Вы не состоите в клане.");
            return false;
        }

        else if (owning == null) {
            player.sendMessage(ChatColor.RED + "Вы не являетесь владельцем клана - " + clan);
            return false;
        }

        List<String> playerslist = config.getStringList("clans." + clan + ".participants");
        for (String item : playerslist) {
            config.set("players." + item + ".inclan?", false);
            config.set("players." + item + ".clan", null);
            _plugin.saveConfig();
        }
        config.set("clans." + clan, null);
        player.sendMessage(ChatColor.GOLD + "Вы успешно удалили клан - " + clan);
        _plugin.saveConfig();
        return false;
    }
}
