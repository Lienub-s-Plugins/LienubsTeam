package lienub.dev.lienubsteam.listeners;

import lienub.dev.lienubsteam.LienubsTeam;
import lienub.dev.lienubsteam.utils.team.Member;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Listens for when a player joins the server.
 * Adds the player's team's permissions to the player.
 * If the player is not in a team, adds the default permissions to the player.
 *
 * @see Listener
 * @see PlayerJoinEvent
 * @see PermissionAttachment
 * @see Member
 * @see LienubsTeam
 * @see List
 * @see String
 */
public class PlayerJoinListener implements Listener {

    private final LienubsTeam plugin;

    /**
     * Instantiates a new Player join listener.
     *
     * @param plugin the plugin
     */
    public PlayerJoinListener(@NonNull LienubsTeam plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * On player join.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PermissionAttachment attachment = event.getPlayer().addAttachment(plugin);

        // get the player's team member object
        Member member = plugin.getTeamManager().getMember(event.getPlayer());
        if (member != null) {
            // add the team's permissions to the player
            for (String permission : member.getRoleEnum().getPermissions()) {
                attachment.setPermission(permission, true);
            }
            event.getPlayer().recalculatePermissions();
        } else {
            // add the default permissions to the player
            List<String> defaultPermissions = List.of("lienub.team.info", "lienub.team.create", "lienub.team.join", "lienub.team.leave");
            for (String permission : defaultPermissions) {
                attachment.setPermission(permission, true);
            }
        }
    }
}