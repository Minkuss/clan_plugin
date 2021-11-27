package me.minkuss.listeners;

import me.minkuss.clan_plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class JoinPlayerListener implements Listener {

    private final clan_plugin _plugin;

    public JoinPlayerListener(clan_plugin plugin) {
        _plugin = plugin;
    }

    @EventHandler
    public void onJoinServer(PlayerJoinEvent event) {
        FileConfiguration config = _plugin.getConfig();
        Player player = event.getPlayer();
        String id = _plugin.getConfig().getString("players." + player.getName());

        if(id == null) {
            config.set("players." + player.getName() + ".inclan?", false);

            _plugin.saveConfig();
        }
    }

}