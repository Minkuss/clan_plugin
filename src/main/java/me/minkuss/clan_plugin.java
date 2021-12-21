package me.minkuss;

import me.minkuss.commands.*;
import me.minkuss.listeners.InviteEventListener;
import me.minkuss.listeners.JoinPlayerListener;
import me.minkuss.tab_completers.ClanTabCompleters;
import me.minkuss.tab_completers.ReqTabCompleters;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class clan_plugin extends JavaPlugin {

    private final Server _server = getServer();
    private final PluginManager _plugin_manager = getServer().getPluginManager();

    @Override
    public void onEnable() {
        SetCommands();
        RegisterEvents();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    private void SetCommands() {
        _server.getPluginCommand("clan").setExecutor(new ClanCommands(this));
        _server.getPluginCommand("clan").setTabCompleter(new ClanTabCompleters(this));

        _server.getPluginCommand("req").setExecutor(new ReqCommands(this));
        _server.getPluginCommand("req").setTabCompleter(new ReqTabCompleters(this));
    }

    private void RegisterEvents() {
        _plugin_manager.registerEvents(new JoinPlayerListener(this), this);
        _plugin_manager.registerEvents(new InviteEventListener(this), this);
        _plugin_manager.registerEvents(new ClanCommands(this), this);
    }
}
