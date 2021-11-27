package me.minkuss.commands;

import me.minkuss.clan_plugin;
import me.minkuss.events.InviteEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class InviteClanCommand implements CommandExecutor {
    private clan_plugin _plugin;
    public InviteClanCommand(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = _plugin.getConfig();
        String playername = _plugin.getServer().getPlayer(args[0]).getName();
        Boolean inclan = config.getBoolean("players." + playername + ".inclan?");
        Player player = (Player) sender;
        String clan = config.getString("players." + player.getName() + ".clan");

        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может отправить только игрок.");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Введите команду полностью /inviteclan <ник игрока>");
            return false;
        }

        else if (args[0] == null) {
            sender.sendMessage(ChatColor.RED + "Введите ник игрока");
            return false;
        }

        if (clan == null) {
            sender.sendMessage(ChatColor.RED + "Вы не состоите в клане");
            return false;
        }

        else if (_plugin.getServer().getPlayer(playername) == null) {
            sender.sendMessage("Такого игрока нет на сервере");
            return false;
        }

        else if (inclan) {
            sender.sendMessage("Этот игрок уже состоит в клане");
            return false;
        }

        if (config.getString("players." + playername + ".acceptingClan") == null) {

            config.set("players." + playername + ".accepting", true);
            config.set("players." + playername + ".acceptingClan", clan);
            config.set("players." + playername + ".inviter", player.getName());

            _plugin.getServer().getPlayer(playername).sendMessage(ChatColor.GOLD + "Вам пришло приглашение от клана - " + clan + " чтобы принять его напишите /accept. Срок 30 секунд.");
            _plugin.saveConfig();
            sender.sendMessage(ChatColor.GOLD + "Вы отправили приглашение игроку - " + playername);
            _plugin.getServer().getPluginManager().callEvent(new InviteEvent(_plugin.getServer().getPlayer(playername), clan));
        }

        else if (config.getString("players." + playername + ".acceptingClan") != null) {
            sender.sendMessage(ChatColor.RED + "Данному игроку уже пришло приглашение, попробуйте еще раз через 30 секунд");
            return false;
        }

        return false;
    }
}
