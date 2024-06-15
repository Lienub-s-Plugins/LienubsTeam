package lienub.dev.lienubsteam.utils.team;

import lienub.dev.lienubsteam.LienubsTeam;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * The member class.
 * Represents a member of a team.
 * Contains the player's UUID and role.
 * Also contains methods to update the player's permissions.
 *
 * @version 1.0.0
 * @see Role
 * @see LienubsTeam
 * @see Player
 * @see PermissionAttachment
 * @see UUID
 * @see Nullable
 * @since 1.0.0
 */
public class Member {
    private final UUID uuid;
    private Role role;

    /**
     * Instantiates a new Member.
     * Also updates the player's permissions, if they are online.
     *
     * @param player the player
     * @param role   the role
     *               (default is MEMBER)
     */
    public Member(UUID player, @Nullable Role role) {
        this.uuid = player;
        this.role = role != null ? role : Role.MEMBER;
        updatePermissions(LienubsTeam.getInstance());
    }

    /**
     * Gets player uuid.
     *
     * @return the player uuid
     * @see UUID
     */
    public UUID getPlayerUUID() {
        return uuid;
    }

    /**
     * Gets role as string.
     *
     * @return the role
     * @see Role
     */
    public String getRole() {
        return role.getRoleName();
    }

    /**
     * Gets role enum.
     *
     * @return the role enum
     * @see Role
     */
    public Role getRoleEnum() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     *             (default is MEMBER)
     * @see Role
     */
    public void setRole(@NotNull String role) {
        this.role = Role.valueOf(role.toUpperCase());
        updatePermissions(LienubsTeam.getInstance());
    }

    private void updatePermissions(@NotNull LienubsTeam plugin) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            plugin.getLogger().info("Updating permissions for " + player.getName());
            PermissionAttachment attachment = player.addAttachment(plugin);

            // Add the new permissions
            for (String permission : role.getPermissions()) {
                attachment.setPermission(permission, true);
            }

            plugin.getPlayerPermissions().put(uuid, attachment);

            player.recalculatePermissions();
        }
    }
}
