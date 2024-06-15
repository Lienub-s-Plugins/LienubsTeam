package lienub.dev.lienubsteam.listeners;

import lienub.dev.lienubsteam.LienubsTeam;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listener for player position.
 * This class listens for player position changes and notifies the player if they enter a team's territory.
 *
 * @see Listener
 * @see PlayerMoveEvent
 * @see Player
 * @see Location
 * @see LienubsTeam
 */
public class PlayerPositionListener implements Listener {
    private final LienubsTeam plugin;

    /**
     * Instantiates a new Player position listener.
     *
     * @param plugin the plugin
     */
    public PlayerPositionListener(@NonNull LienubsTeam plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * On chunk enter.
     *
     * @param event the event
     */
    @EventHandler
    public void onChunkEnter(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo() != null ? event.getTo() : from;

        if (!from.getChunk().equals(to.getChunk())) {
            if (plugin.getTeamManager().getTeamByChunk(to.getChunk()) != null) {
                player.sendMessage("You entered " + plugin.getTeamManager().getTeamByChunk(to.getChunk()).getName() + "'s territory");
            } else {
                player.sendMessage("You are in the wilderness");
            }
        }
    }
}
