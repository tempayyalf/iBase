package com.doctordark.base.kit;

import com.doctordark.base.kit.event.KitApplyEvent;
import com.doctordark.util.GenericUtils;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a {@link Kit} that can be applied to a {@link Player}.
 */
public class Kit implements ConfigurationSerializable {

    private static final ItemStack DEFAULT_IMAGE = new ItemStack(Material.EMERALD, 1);

    private final UUID uniqueID;
    private String name;
    private String description;
    private ItemStack[] items;
    private ItemStack[] armour;
    private Collection<PotionEffect> effects;
    private ItemStack image;
    private boolean enabled = true;
    private long delayMillis;
    private int maximumUses;

    /**
     * Constructs a new {@link Kit} from scratch.
     *
     * @param name        the name to use for this {@link Kit}
     * @param description the description for this {@link Kit}
     * @param inventory   the {@link Inventory} for this {@link Kit}
     * @param effects     the collection of {@link PotionEffect}s
     */
    public Kit(String name, String description, PlayerInventory inventory, Collection<PotionEffect> effects) {
        this(name, description, inventory, effects, 0L);
    }

    /**
     * Constructs a new {@link Kit} from scratch.
     *
     * @param name         the name to use for this {@link Kit}
     * @param description  the description for this {@link Kit}
     * @param inventory    the {@link Inventory} of this {@link Kit}
     * @param effects      the collection of {@link PotionEffect}s
     * @param milliseconds the delay in milliseconds to equip this {@link Kit}
     */
    public Kit(String name, String description, Inventory inventory, Collection<PotionEffect> effects, long milliseconds) {
        this.uniqueID = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.setItems(inventory.getContents());
        if (inventory instanceof PlayerInventory) {
            PlayerInventory playerInventory = (PlayerInventory) inventory;
            this.setArmour(playerInventory.getArmorContents());
            this.setImage(playerInventory.getItemInHand());
        } else {
            this.setArmour(new ItemStack[InventoryType.PLAYER.getDefaultSize()]);
        }

        this.effects = effects;
        this.delayMillis = milliseconds;
        this.maximumUses = FlatFileKitManager.UNLIMITED_USES;
    }

    /**
     * Constructs a {@link Kit} from a map.
     *
     * @param map the map to construct from
     */
    public Kit(Map<String, Object> map) {
        this.uniqueID = map.containsKey("uniqueID") ? UUID.fromString((String) map.get("uniqueID")) : UUID.randomUUID();

        this.name = (String) map.get("name");
        this.description = (String) map.get("description");
        this.enabled = (boolean) (Boolean) map.get("enabled");
        this.effects = GenericUtils.createList(map.get("effects"), PotionEffect.class);

        List<ItemStack> items = GenericUtils.createList(map.get("items"), ItemStack.class);
        this.items = items.toArray(new ItemStack[items.size()]);

        List<ItemStack> armour = GenericUtils.createList(map.get("armour"), ItemStack.class);
        this.armour = armour.toArray(new ItemStack[armour.size()]);

        this.setImage((ItemStack) map.get("image"));
        this.delayMillis = Long.parseLong((String) map.get("delay"));
        this.maximumUses = (Integer) map.get("maxUses");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = Maps.newLinkedHashMap();
        if (uniqueID != null) map.put("uniqueID", uniqueID.toString());
        map.put("name", name);
        map.put("description", description);
        map.put("enabled", enabled);
        map.put("effects", effects);
        map.put("items", items);
        map.put("armour", armour);
        map.put("image", image);
        map.put("delay", Long.toString(delayMillis));
        map.put("maxUses", maximumUses);
        return map;
    }

    /**
     * Gets the unique ID of this {@link Kit}.
     *
     * @return the uuid
     */
    public UUID getUniqueID() {
        return uniqueID;
    }

    /**
     * Gets the name of this {@link Kit}.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this {@link Kit}.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of this {@link Kit}.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this {@link Kit}.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the items of this {@link Kit}.
     *
     * @return the array of items
     */
    public ItemStack[] getItems() {
        return Arrays.copyOf(items, items.length);
    }

    /**
     * Sets the items for this {@link Kit}.
     *
     * @param items the items to set
     */
    public void setItems(ItemStack[] items) {
        int length = items.length;
        this.items = new ItemStack[length];
        for (int i = 0; i < length; i++) {
            ItemStack next = items[i];
            this.items[i] = next == null ? null : next.clone();
        }
    }

    /**
     * Gets the armour for this {@link Kit}.
     *
     * @return the {@link Kit} armour
     */
    public ItemStack[] getArmour() {
        return Arrays.copyOf(armour, armour.length);
    }

    /**
     * Sets the armour for this {@link Kit}.
     *
     * @param armour the armour to set
     */
    public void setArmour(ItemStack[] armour) {
        int length = armour.length;
        this.armour = new ItemStack[length];
        for (int i = 0; i < length; i++) {
            ItemStack next = armour[i];
            this.armour[i] = next == null ? null : next.clone();
        }
    }

    /**
     * Gets the image of this {@link Kit}.
     *
     * @return the {@link ItemStack} image
     */
    public ItemStack getImage() {
        if (image == null || image.getType() == Material.AIR) {
            this.image = DEFAULT_IMAGE;
        }

        return image;
    }

    /**
     * Sets the {@link ItemStack} image of this {@link Kit}.
     *
     * @param image the image to set
     */
    public void setImage(ItemStack image) {
        this.image = image == null || image.getType() == Material.AIR ? null : image.clone();
    }

    /**
     * Gets the {@link PotionEffect}s in this {@link Kit}.
     *
     * @return collection of {@link PotionEffect}s
     */
    public Collection<PotionEffect> getEffects() {
        return effects;
    }

    /**
     * Sets the {@link PotionEffect}s for this kit.
     *
     * @param effects the potion effects to set
     */
    public void setEffects(Collection<PotionEffect> effects) {
        this.effects = effects;
    }

    /**
     * Checks if this {@link Kit} is enabled for public use.
     *
     * @return true if {@link Kit} is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets if this {@link Kit} is enabled.
     *
     * @param enabled the value to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the delay for using this {@link Kit} in milliseconds.
     *
     * @return the {@link Kit} use delay in milliseconds
     */
    public long getDelayMillis() {
        return delayMillis;
    }

    /**
     * Sets the delay for using this {@link Kit} in milliseconds.
     *
     * @param delayMillis the delay to set in milliseconds
     */
    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    /**
     * Gets the maximum uses for this {@link Kit}.
     *
     * @return the maximum uses
     */
    public int getMaximumUses() {
        return maximumUses;
    }

    /**
     * Sets the maximum uses for this {@link Kit}.
     *
     * @param maximumUses the maximum uses to set
     */
    public void setMaximumUses(int maximumUses) {
        this.maximumUses = maximumUses;
    }

    /**
     * Gets the permission required to use this {@link Kit}.
     *
     * @return the {@link Kit} permission
     */
    public final String getPermission() {
        return "hcf.kit." + name;
    }

    /**
     * Applies this {@link Kit} to a {@link Player} with specific conditions.
     *
     * @param player        the {@link Player} to apply to
     * @param force         if it should ignore conditions
     * @return true if the {@link Kit} was successfully applied
     */
    public boolean applyTo(Player player, boolean force, boolean inform) {
        KitApplyEvent event = new KitApplyEvent(this, player, force);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }

        // Prevent duping items.
        player.closeInventory();
        PlayerInventory inventory = player.getInventory();
        player.addPotionEffects(effects);

        Location location = player.getLocation();
        World world = player.getWorld();
        for (ItemStack item : items) {
            if (item == null || item.getType() == Material.AIR) continue;
            for (Map.Entry<Integer, ItemStack> excess : inventory.addItem(item.clone()).entrySet()) {
                world.dropItemNaturally(location, excess.getValue());
            }
        }

        for (int i = Math.min(3, armour.length); i >= 0; i--) {
            ItemStack stack = armour[i];
            if (stack == null || stack.getType() == Material.AIR) {
                continue;
            }

            int armourSlot = (i + 36);
            ItemStack previous = inventory.getItem(armourSlot);
            if (previous != null && previous.getType() != Material.AIR) {
                world.dropItemNaturally(location, stack.clone());
            } else {
                inventory.setItem(armourSlot, stack.clone());
            }
        }

        if (inform) {
            player.sendMessage(ChatColor.AQUA + "Kit " + ChatColor.GREEN + name + ChatColor.AQUA + " has been applied.");
        }

        return true;
    }
}