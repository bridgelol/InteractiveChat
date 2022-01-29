package com.loohp.interactivechat.api.events;

import com.loohp.interactivechat.objectholders.OfflineICPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This is fired when an offlineICPlayer is created
 *
 * @author LOOHP
 */
public class OfflineICPlayerCreationEvent extends OfflineICPlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public OfflineICPlayerCreationEvent(OfflineICPlayer player) {
        super(player);
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
