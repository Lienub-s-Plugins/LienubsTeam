package lienub.dev.lienubsteam.listeners;

import lienub.dev.lienubsteam.LienubsTeam;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        // Check if the chunk is claimed
        if (plugin.teamManager.getTeamByChunk(event.getClickedBlock().getChunk()) != null) {
            // Check if the player is a member of the team
            if (plugin.teamManager.getTeamByChunk(event.getClickedBlock().getChunk()).getMembers().stream().anyMatch(member -> member.getPlayerUUID().equals(event.getPlayer().getUniqueId()))) {
                return;
            }
            // Prevent the interaction
            event.setCancelled(true);
            event.getPlayer().spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu ne peux pas interagir dans le territoire de cette équipe."));
        }
    }
}