package com.doctordark.base.kit.event;

import com.doctordark.base.kit.Kit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event called when a {@link Kit} has been removed.
 */
public class KitRemoveEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private final Kit kit;

    public KitRemoveEvent(Kit kit) {
        this.kit = kit;
    }

    /**
     * Gets the {@link Kit} being removed.
     *
     * @return the removed {@link Kit}
     */
    public Kit getKit() {
        return kit;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}