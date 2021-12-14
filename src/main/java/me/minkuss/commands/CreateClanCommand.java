package me.minkuss.commands;

import me.minkuss.clan_plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateClanCommand implements CommandExecutor {
    private clan_plugin _plugin;
    public CreateClanCommand(clan_plugin plugin) {
        _plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return  false;
        }
        // /createclan <название>
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Введите команду полностью /createclan <название_клана>");
            return false;
        }

        else if (args.length == 1) {
            FileConfiguration config = _plugin.getConfig();
            String clanName = args[0];
            Player player = (Player) sender;
            Boolean inClan = config.getBoolean("players." + player.getName() + ".inclan?");
            if (!inClan) {
                List<String> playerlist = List.of(player.getName());
                List<String> clanlist = config.getStringList("clanlist");
                if (clanlist.contains(clanName)) {
                    player.sendMessage(ChatColor.RED + "Клан с таким названием уже существует.");
                    return false;
                }
                clanlist.add(clanName);
                config.createSection("players." + player.getName() + ".inclan?");
                config.createSection("players." + player.getName() + ".clan");
                config.createSection("clans." + clanName + ".participants");
                config.createSection("clans." + clanName + ".owners");
                config.createSection("clans." + clanName + ".clanmates");

                config.set("clans." + clanName + ".clanmates", 1);
                config.set("clans." + clanName + ".participants", playerlist);
                config.set("clans." + clanName + ".owners", player.getName());
                config.set("clanlist", clanlist);

                config.set("players." + player.getName() + ".inclan?", true);
                config.set("players." + player.getName() + ".clan", clanName);
                _plugin.saveConfig();
                player.sendMessage(ChatColor.GOLD + "Вы создали клан - " + clanName);
            }
            else if (inClan) {
                player.sendMessage(ChatColor.RED + "Вы уже состоите в клане, чтобы выйти из клана используйте команду /leaveclan <название_клана>");
            }
        }
        return false;
    }
}
