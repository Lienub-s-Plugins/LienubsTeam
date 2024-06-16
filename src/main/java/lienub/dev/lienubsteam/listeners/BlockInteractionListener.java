package lienub.dev.lienubsteam.listeners;

import lienub.dev.lienubsteam.LienubsTeam;
import lienub.dev.lienubsteam.utils.TextComponentUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens for block interactions.
 * Prevents players from interacting with blocks in claimed chunks.
 *
 * @see Listener
 * @see PlayerInteractEvent
 * @see EventHandler
 * @see TextComponentUtil
 * @see LienubsTeam
 * @see BlockInteractionListener
 * @since 1.0
 * @version 1.0
 *
 */
public class BlockInteractionListener implements Listener {
    private final LienubsTeam plugin;

    /**
     * Instantiates a new Block interaction listener.
     *
     * @param plugin the plugin
     */
    public BlockInteractionListener(@NonNull LienubsTeam plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * On player interact.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }

        // Check if the chunk is claimed
        if (plugin.getTeamManager().getTeamByChunk(event.getClickedBlock().getChunk()) != null) {
            // Check if the player is a member of the team
            if (plugin.getTeamManager().getTeamByChunk(event.getClickedBlock().getChunk()).getMembers().stream().anyMatch(member -> member.getPlayerUUID().equals(event.getPlayer().getUniqueId()))) {
                return;
            }
            // Prevent the interaction
            event.setCancelled(true);
            event.getPlayer().spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu ne peux pas interagir dans le territoire de cette équipe."));
        }
    }
}