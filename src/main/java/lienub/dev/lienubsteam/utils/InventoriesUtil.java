package lienub.dev.lienubsteam.utils;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Utility class for inventories.
 *
 * @version 1.0
 * @see org.bukkit.inventory.Inventory
 * @see org.bukkit.inventory.ItemStack
 * @see org.bukkit.inventory.meta.SkullMeta
 * @see org.bukkit.OfflinePlayer
 * @see org.bukkit.Material
 * @see org.bukkit.inventory.meta.ItemMeta
 * @see org.bukkit.inventory.InventoryHolder
 * @since 1.0
 */
public class InventoriesUtil {

    private InventoriesUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Sets inventory outline.
     * It sets a border around the inventory with the given items in the range of the rows.
     *
     * @param rows      the number of rows to set the outline
     * @param inventory the inventory
     * @param items     the items to set the outline with
     */
    public static void setupInventoryOutline(int rows, Inventory inventory, ItemStack... items) {
        int columns = 9;
        int size = rows * columns;

        // Set the first and last row of the inventory with items.
        for (int i = 0; i < columns; i++) {
            inventory.setItem(i, items[i % items.length]);
            inventory.setItem(i + size - columns, items[i % items.length]);
        }

        // Set the first and last column of the inventory with items.
        for (int i = columns; i < size - columns; i += columns) {
            inventory.setItem(i, items[i % items.length]);
            inventory.setItem(i + columns - 1, items[i % items.length]);
        }
    }

    /**
     * Gets player head.
     * Return a default player head with the given player if the player head is not found.
     *
     * @param player the player
     * @return the player head
     */
    public static @NotNull ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        if (skullMeta == null) {
            return skull;
        }

        skullMeta.setOwningPlayer(player);
        skull.setItemMeta(skullMeta);

        return skull;
    }
    /**
     * Initializes item meta.
     * It sets the display name and lore of the item.
     *
     * @param item        the item
     * @param displayName the display name
     * @param lore        the lore
     * @return the item meta
     */
    public static @Nullable ItemMeta initItemMeta(@NotNull ItemStack item, @Nullable String displayName, @Nullable List<String> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return null;
        }

        if (displayName != null) {
            itemMeta.setDisplayName(displayName);
        }

        if (lore != null) {
            itemMeta.setLore(lore);
        }

        item.setItemMeta(itemMeta);
        return itemMeta;
    }
}
