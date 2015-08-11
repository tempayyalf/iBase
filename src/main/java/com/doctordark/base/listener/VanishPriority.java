package com.doctordark.base.listener;

import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Player;

/**
 * Represents a priority of vanishing.
 */
public enum VanishPriority {

    HIGHEST(6), HIGH(5), MEDIUM(4), NORMAL(3), LOW(2), LOWEST(1), NONE(0);

    private final int priorityLevel;

    VanishPriority(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    private static final ImmutableMap<Integer, VanishPriority> BY_ID;

    static {
        ImmutableMap.Builder<Integer, VanishPriority> builder = new ImmutableMap.Builder<>();
        for (VanishPriority vanishPriority : values()) {
            builder.put(vanishPriority.priorityLevel, vanishPriority);
        }

        BY_ID = builder.build();
    }

    /**
     * Gets the ID level for this {@link VanishPriority}.
     *
     * @return the priority level
     */
    public int getPriorityLevel() {
        return this.priorityLevel;
    }

    /**
     * Checks if this {@link VanishPriority} has more priority that another {@link VanishPriority}.
     *
     * @param other the other {@link VanishPriority} to compare to
     * @return true if this {@link VanishPriority} is more than the other
     */
    public boolean isMoreThan(VanishPriority other) {
        return this.priorityLevel > other.priorityLevel;
    }

    /**
     * Gets the {@link VanishPriority} for a specific level.
     *
     * @param level the level to get for
     * @return the {@link VanishPriority} of level, or {@code NONE}
     */
    public static VanishPriority of(int level) {
        return BY_ID.get(level);
    }

    /**
     * Gets the {@link VanishPriority} of a specific {@link Player}.
     *
     * @param player the {@link Player} to get for
     * @return the {@link VanishPriority} of {@link Player}, or {@code NONE}
     */
    public static VanishPriority of(Player player) {
        for (VanishPriority vanishPriority : values()) {
            if (player.hasPermission("base.vanishpriority." + vanishPriority.priorityLevel)) {
                return vanishPriority;
            }
        }

        return NONE;
    }
}
