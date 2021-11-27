package me.minkuss.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InviteEvent extends Event {

    private final Player _player;
    private final static HandlerList _handlers = new HandlerList();
    private final String _acceptingclan;

    public InviteEvent(Player player, String acceptingclan) {
        _player = player;
        _acceptingclan = acceptingclan;
    }

    public Player getPlayer() {
        return _player;
    }


    public String getAcceptClan() {
        return _acceptingclan;
    }

    @Override
    public HandlerList getHandlers() {
        return _handlers;
    }
    public static HandlerList getHandlerList() { return _handlers; };
}
