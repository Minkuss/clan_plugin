package me.minkuss.tab_completers;

import me.minkuss.clan_plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class ReqTabCompleters implements TabCompleter {
    private final clan_plugin _plugin;
    public ReqTabCompleters(clan_plugin plugin) {
        _plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(args.length == 1) {
            return _plugin.getConfig().getStringList("req-commands");
        }

        return null;
    }
}
