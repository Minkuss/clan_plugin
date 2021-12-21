package me.minkuss.listeners;

import me.minkuss.clan_plugin;
import me.minkuss.events.InviteEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class InviteEventListener implements Listener {
    private final clan_plugin _plugin;

    public InviteEventListener(clan_plugin plugin) {
        _plugin = plugin;
    }

    @EventHandler

    public void PlayerInvite(InviteEvent event) {
        FileConfiguration config = _plugin.getConfig();
        Player player = event.getPlayer();

        new BukkitRunnable() {
            int accept_time = 30;
            @Override
            public void run() {
                if (accept_time == 0) {
                    config.set("players." + player.getName() + ".accepting", null);
                    config.set("players." + player.getName() + ".acceptingClan", null);
                    config.set("players." + player.getName() + ".inviter", null);
                    _plugin.saveConfig();
                    player.sendMessage("Время приглашения истекло...");
                    cancel();
                }
                else {
                    accept_time -= 5;
                }
            }
        }.runTaskTimer(_plugin, 0, 100);

    }
}
