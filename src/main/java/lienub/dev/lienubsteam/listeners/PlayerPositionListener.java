package lienub.dev.lienubsteam.listeners;

import lienub.dev.lienubsteam.LienubsTeam;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
public class PlayerPositionListener implements Listener {
    private final LienubsTeam plugin;

    public PlayerPositionListener(@NonNull LienubsTeam plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChunkEnter(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo() != null ? event.getTo() : from;

        if (!from.getChunk().equals(to.getChunk())) {
            if(plugin.teamManager.getTeamByChunk(to.getChunk()) != null) {
                player.sendMessage("You entered " + plugin.teamManager.getTeamByChunk(to.getChunk()).getName() + "'s territory");
            } else {
                player.sendMessage("You are in the wilderness");
            }
        }
    }
}
