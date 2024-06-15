package lienub.dev.lienubsteam.utils.team;

import org.bukkit.entity.Player;
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
        return role.getRole();
    }

    public void setRole(String role) {
        this.role = Role.valueOf(role);
    }
}
