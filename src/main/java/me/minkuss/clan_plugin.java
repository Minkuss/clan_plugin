package me.minkuss;

import me.minkuss.commands.*;
import me.minkuss.listeners.InviteEventListener;
import me.minkuss.listeners.JoinPlayerListener;
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
        _server.getPluginCommand("createclan").setExecutor(new CreateClanCommand(this));
        _server.getPluginCommand("inviteclan").setExecutor(new InviteClanCommand(this));
        _server.getPluginCommand("accept").setExecutor(new AcceptInviteCommand(this));
        _server.getPluginCommand("leaveclan").setExecutor(new LeaveClanCommand(this));
        _server.getPluginCommand("deleteclan").setExecutor(new DeleteClanCommand(this));
        _server.getPluginCommand("clankick").setExecutor(new ClanKickCommand(this));
        _server.getPluginCommand("clanlist").setExecutor(new ClanListCommand(this));
        _server.getPluginCommand("claninfo").setExecutor(new ClanInfoCommand(this));
        _server.getPluginCommand("joinclan").setExecutor(new JoinClanCommand(this));
        _server.getPluginCommand("requests").setExecutor(new JoinNotificationCommand(this));
    }

    private void RegisterEvents() {
        _plugin_manager.registerEvents(new JoinPlayerListener(this), this);
        _plugin_manager.registerEvents(new InviteEventListener(this), this);
    }
}
