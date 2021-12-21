package me.minkuss.commands;

import me.minkuss.clan_plugin;
import me.minkuss.events.InviteEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class ClanCommands implements CommandExecutor {
    private final clan_plugin _plugin;
    public ClanCommands(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Чтобы увидеть перечень всех комманд, напишите /clan help");
            return false;
        }

        if (args[0].equals("help")) {
            List<String> commands = List.of("/clan help", "/clan create <название>", "/clan list", "/clan info <клан>", "/clan invite <ник>", "/clan accept", "/clan kick <ник>", "/clan delete", "/clan leave", "/clan join <название>", "/clan requests");
            sender.sendMessage(ChatColor.BLUE + "[Список комманд]: ");
            for (String item : commands) {
                sender.sendMessage(ChatColor.GREEN + item);
            }
            return false;
        }

        if (args[0].equals("create")) {
            FileConfiguration config = _plugin.getConfig();
            Player player = (Player) sender;
            boolean inClan = config.getBoolean("players." + player.getName() + ".inclan?");
            if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Введите название своего клана");
                return false;
            }

            else if (args.length == 2) {
                String clanName = args[1];
                if (!inClan) {
                    List<String> playerlist = List.of(player.getName());
                    List<String> clanlist = config.getStringList("clanlist");
                    if (clanlist.contains(clanName)) {
                        player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Клан с таким названием уже существует");
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
                    player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Вы уже состоите в клане");
                }
            }
            return false;
        }

        if (args[0].equals("delete")) {

            FileConfiguration config = _plugin.getConfig();
            Player player = (Player) sender;
            String clan = config.getString("players." + player.getName() + ".clan");
            boolean inclan = config.getBoolean("players." + player.getName() + ".inclan?");
            String owning = config.getString("clans." + clan + ".owners");
            List<String> clanlist = config.getStringList("clanlist");

            if (!inclan) {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Вы не состоите в клане");
                return false;
            }

            else {
                assert owning != null;
                if (!(owning.equals(player.getName()))) {
                    player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Вы не являетесь владельцем клана - " + clan);
                    return false;
                }
            }

            List<String> playerslist = config.getStringList("clans." + clan + ".participants");
            for (String item : playerslist) {
                config.set("players." + item + ".inclan?", false);
                config.set("players." + item + ".clan", null);
                _plugin.saveConfig();
            }
            clanlist.remove(clan);
            config.set("clanlist", clanlist);
            config.set("clans." + clan, null);
            player.sendMessage(ChatColor.GOLD + "Вы успешно удалили клан - " + clan);
            _plugin.saveConfig();
            return false;
        }

        if (args[0].equals("invite")) {

            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Введите ник игрока");
                return false;
            }

            else if (args.length == 2) {

                FileConfiguration config = _plugin.getConfig();
                String playername = _plugin.getServer().getPlayer(args[1]).getName();
                boolean inclan = config.getBoolean("players." + playername + ".inclan?");
                Player player = (Player) sender;
                String clan = config.getString("players." + player.getName() + ".clan");

                if (clan == null) {
                    sender.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Вы не состоите в клане");
                    return false;
                } else if (_plugin.getServer().getPlayer(playername) == null) {
                    sender.sendMessage("Такого игрока нет на сервере");
                    return false;
                } else if (inclan) {
                    sender.sendMessage("Этот игрок уже состоит в клане");
                    return false;
                }

                if (config.getString("players." + playername + ".acceptingClan") == null) {

                    config.set("players." + playername + ".accepting", true);
                    config.set("players." + playername + ".acceptingClan", clan);
                    config.set("players." + playername + ".inviter", player.getName());

                    _plugin.getServer().getPlayer(playername).sendMessage(ChatColor.GOLD + "Вам пришло приглашение от клана - " + clan + " чтобы принять его напишите /clan accept. Срок 30 секунд.");
                    _plugin.saveConfig();
                    sender.sendMessage(ChatColor.GOLD + "Вы отправили приглашение игроку - " + playername);
                    _plugin.getServer().getPluginManager().callEvent(new InviteEvent(_plugin.getServer().getPlayer(playername), clan));
                } else if (config.getString("players." + playername + ".acceptingClan") != null) {
                    sender.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Данному игроку уже пришло приглашение, попробуйте еще раз через 30 секунд");
                    return false;
                }
            }
            return false;
        }

        if (args[0].equals("accept")) {

            FileConfiguration config = _plugin.getConfig();
            Player player = (Player) sender;
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

            if (!accepting) {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "У вас нет активных приглашений");
                return false;
            }

            return false;
        }

        if (args[0].equals("leave")) {

            FileConfiguration config = _plugin.getConfig();
            Player player = (Player) sender;
            String clan = config.getString("players." + player.getName() + ".clan");
            boolean inclan = config.getBoolean("players." + player.getName() + ".inclan?");
            int mates_nubmer = config.getInt("clans." + clan + ".clanmates");
            List<String> clanlist = config.getStringList("clanlist");

            if (inclan) {
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

            if (!inclan) {
                player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Вы не состоите в клане");
                return false;
            }

            return false;
        }

        if (args[0].equals("kick")) {

            FileConfiguration config = _plugin.getConfig();
            Player playersender = (Player) sender;
            String clan = config.getString("players." + playersender.getName() + ".clan");
            boolean inclan = config.getBoolean("players." + playersender.getName() + ".inclan?");
            String owning = config.getString("clans." + clan + ".owners");
            int mates_nubmer = config.getInt("clans." + clan + ".clanmates");

            if (args.length == 1) {
                playersender.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Введите имя игрока, которого хотите исключить");
                return false;
            }

            if (!inclan) {
                playersender.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Вы не состоите в клане");
                return false;
            }

            else if (!(owning.equals(playersender.getName()))) {
                playersender.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Вы не являетесь владельцем клана");
                return false;
            }

            else if (args.length == 2) {

                String playerkick = args[1];

                if (playersender.getName().equals(playerkick)) {
                    playersender.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Вы не можете исключить самого себя");
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
                } else if (_plugin.getServer().getPlayer(playerkick) == null) {
                    config.set("players." + playerkick + ".massage", "К сожалению, вас исключили из клана. Зря они так поступили(");
                }
                _plugin.saveConfig();
            }
            return false;
        }

        if (args[0].equals("list")) {

            FileConfiguration config = _plugin.getConfig();
            List<String> clanlist = config.getStringList("clanlist");

            sender.sendMessage(ChatColor.BLUE + "[Список кланов]: ");

            for (String item : clanlist) {
                sender.sendMessage(ChatColor.GOLD + item);
            }

            return false;
        }

        if (args[0].equals("info")) {

            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Введите название клана");
                return false;
            }

            else if (args.length == 2) {

                Player player =  (Player) sender;
                FileConfiguration config = _plugin.getConfig();
                String clanName = args[1];
                List<String> clanlist = config.getStringList("clanlist");
                String owner = config.getString("clans." + clanName + ".owners");
                int clanmates = config.getInt("clans." + clanName + ".clanmates");
                List<String> participants = config.getStringList("clans." + clanName + ".participants");

                if (!(clanlist.contains(clanName))) {
                    player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Такого клана не существует");
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
            }
            return false;
        }

        if (args[0].equals("join")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Введите название клана");
                return false;
            }

            else if (args.length == 2) {

                Player player = (Player) sender;
                FileConfiguration config = _plugin.getConfig();
                String clan = args[1];
                List<String> clanlist = config.getStringList("clanlist");
                String owner = config.getString("clans." + clan + ".owners");
                List<String> joins = config.getStringList("players." + owner + ".joiners");
                boolean inclan = config.getBoolean("players." + player.getName() + ".inclan?");

                if (!(clanlist.contains(clan))) {
                    player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Такого клана не существует");
                    return false;
                }

                if (inclan) {
                    player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Вы уже состоите в клане");
                    return false;
                }

                if (joins != null) {
                    if (joins.contains(player.getName())) {
                        player.sendMessage(ChatColor.RED + "[Error] " + ChatColor.GOLD + "Вы уже отправили приглашение в этот клан");
                        return false;
                    }
                    joins.add(player.getName());
                    config.set("players." + owner + ".joiners", joins);
                    player.sendMessage(ChatColor.GOLD + "Вы успешно отправили приглашение");
                    _plugin.saveConfig();
                    return false;
                } else if (joins == null) {
                    List<String> joinz = List.of(player.getName());
                    config.set("players." + owner + ".joiners", joinz);
                    player.sendMessage(ChatColor.GOLD + "Вы успешно отправили приглашение");
                    _plugin.saveConfig();
                    return false;
                }
            }
            return false;
        }

        if (args[0].equals("requests")) {

            FileConfiguration config = _plugin.getConfig();
            Player player = (Player) sender;
            List<String> joins = config.getStringList("players." + player.getName() + ".joiners");
            boolean isField = config.contains("players." + player.getName() + ".joiners");

            if (!isField) {
                player.sendMessage(ChatColor.GOLD + "У вас нет активных запросов");
                return false;
            }

            player.sendMessage(ChatColor.BLUE + "[Список запросов]: ");
            for (String item : joins) {
                player.sendMessage(ChatColor.GOLD + item);
            }

            player.sendMessage(ChatColor.GREEN + "[Info]" + ChatColor.GOLD + "Чтобы принять одного человека в клан, напишите /req accept <ник>. Чтобы принять всех, напишите /req accept, чтобы отчистить, напишите /req clean");

            return false;
        }

        return false;
    }
}
