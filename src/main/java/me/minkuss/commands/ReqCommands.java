package me.minkuss.commands;

import me.minkuss.clan_plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class ReqCommands implements CommandExecutor {
    private final clan_plugin _plugin;
    public ReqCommands(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return false;
        }

        FileConfiguration config = _plugin.getConfig();
        boolean isField = config.contains("players." + sender.getName() + ".joiners");

        if (!isField) {
            sender.sendMessage(ChatColor.GOLD + "У вас нет активных запросов");
            return false;
        }

        if (args[0].equals("clean")) {
            Player player = (Player) sender;

            config.set("players." + player.getName() + ".joiners", null);
            player.sendMessage(ChatColor.GREEN + "Вы успешно очистили список запросов");
            List<String> joins = config.getStringList("players." + player.getName() + ".joiners");

            for (String item : joins) {
                if (_plugin.getServer().getPlayer(item) != null) {
                    _plugin.getServer().getPlayer(item).sendMessage(ChatColor.RED + "К сожалению, ваш запрос на вступление в клан отклонили");
                }
                else if (_plugin.getServer().getPlayer(item) == null) {
                    config.set("players." + item + ".massage", "К сожалению, ваш запрос на вступление в клан отклонили");
                }
            }

            _plugin.saveConfig();

            return false;
        }

        if (args[0].equals("accept")) {
            if (args.length == 2) {
                String player = args[1];
                Player playerSender = (Player) sender;
                List<String> joins = config.getStringList("players." + playerSender.getName() + ".joiners");
                boolean isPlayer = joins.contains(player);
                if (isPlayer) {
                    String clanName = config.getString("players." + playerSender.getName() + ".clan");
                    int mates_nubmer = config.getInt("clans." + clanName + ".clanmates");
                    List<String> clanplayers = config.getStringList("clans." + clanName + ".participants");

                    config.set("clans." + clanName + ".clanmates", mates_nubmer + 1);
                    clanplayers.add(player);
                    config.set("clans." + clanName + ".participants", clanplayers);
                    config.set("players." + player + ".inclan?", true);
                    config.set("players." + player + ".clan", clanName);

                    playerSender.sendMessage(ChatColor.GREEN + "Вы успешно добавили игрока");

                    joins.remove(player);
                    config.set("players." + playerSender.getName() + ".joiners", joins);

                    if (_plugin.getServer().getPlayer(player) != null) {
                        _plugin.getServer().getPlayer(player).sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "Вас приняли в клан - " + clanName + "!");
                    }

                    else if (_plugin.getServer().getPlayer(player) == null) {
                        config.set("players." + player + ".massage", "Вас приняли в клан - " + clanName);
                    }

                    _plugin.saveConfig();
                }
                if (!isPlayer) {
                    playerSender.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Такого игрока нет в запросах");
                    return false;
                }
                return false;
            }

            if (args.length == 1) {
                Player playerSender = (Player) sender;
                List<String> joins = config.getStringList("players." + playerSender.getName() + ".joiners");
                String clanName = config.getString("players." + playerSender.getName() + ".clan");
                int mates_nubmer = config.getInt("clans." + clanName + ".clanmates");
                List<String> clanplayers = config.getStringList("clans." + clanName + ".participants");
                int playersnum = joins.size();

                clanplayers.addAll(joins);

                config.set("clans." + clanName + ".participants", clanplayers);
                config.set("clans." + clanName + ".clanmates", mates_nubmer + playersnum);

                playerSender.sendMessage(ChatColor.GREEN + "Вы успешно добавили всех игроков");

                for (String item : joins) {
                    config.set("players." + item + ".inclan?", true);
                    config.set("players." + item + ".clan", clanName);

                    if (_plugin.getServer().getPlayer(item) != null) {
                        _plugin.getServer().getPlayer(item).sendMessage(ChatColor.GREEN + "[Info] " + ChatColor.GOLD + "Вас приняли в клан - " + clanName + "!");
                    }
                    else if (_plugin.getServer().getPlayer(item) == null) {
                        config.set("players." + item + ".massage", "Вас приняли в клан - " + clanName);
                    }
                }

                config.set("players." + playerSender.getName() + ".joiners", null);

                _plugin.saveConfig();

                return false;
            }
        }

        return false;
    }
}
