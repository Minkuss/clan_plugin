package me.minkuss.tab_completers;

import me.minkuss.clan_plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class ClanTabCompleters implements TabCompleter {
    private final clan_plugin _plugin;
    public ClanTabCompleters(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(args.length == 1) {
            return _plugin.getConfig().getStringList("clan-commands");
        }

        return null;
    }
}
