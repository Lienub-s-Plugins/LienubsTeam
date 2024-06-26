package lienub.dev.lienubsteam.listeners;

import lienub.dev.lienubsteam.LienubsTeam;
import lienub.dev.lienubsteam.holders.TeamInfoHolder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * Listens for inventory click events.
 *
 * @since 1.0
 * @version 1.0
 * @see Listener
 * @see InventoryClickEvent
 * @see Inventory
 * @see ItemStack
 * @see TeamInfoHolder
 * @see LienubsTeam
 * @see EventHandler
 */
public class TeamInfoListener implements Listener {
    /**
     * Instantiates a new Team info listener.
     *
     * @param plugin the plugin
     */
    public TeamInfoListener(@NonNull LienubsTeam plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * On inventory click.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        // Add a null check in case the player clicked outside the window.
        if (!(inventory.getHolder() instanceof TeamInfoHolder)) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        // Change the page if the player clicked the next/previous page item.
        itemLogic(clickedItem, inventory, player);
    }

    private void itemLogic(ItemStack clickedItem, Inventory inventory, Player player) {
        if (clickedItem != null && clickedItem.getItemMeta() != null) {
            TeamInfoHolder holder = (TeamInfoHolder) inventory.getHolder();

            if (holder == null) {
                return;
            }

            switch (clickedItem.getItemMeta().getDisplayName()) {
                case "Page précédente":
                    holder.previousPage();
                    break;
                case "Page suivante":
                    holder.nextPage();
                    break;
                default:
                    break;
            }

            // If item contains a nbt with key "onlinemember" then make the clicker request a tp to the player
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta != null) {
                PersistentDataContainer container = meta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(JavaPlugin.getPlugin(LienubsTeam.class), "onlinemember");
                if (container.has(key, PersistentDataType.STRING)) {
                   player.performCommand("team tpa " + ChatColor.stripColor(meta.getDisplayName()));
                   player.closeInventory();
                }
            }
        }
    }

}
