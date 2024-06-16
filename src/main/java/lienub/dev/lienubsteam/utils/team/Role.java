package lienub.dev.lienubsteam.utils.team;

import java.util.List;

/**
 * The enum Role represents the role of a player in a team.
 */
public enum Role {
    /**
     * Leader role.
     * Leader has all permissions regarding the team management.
     */
    LEADER("leader"),
    /**
     * Moderator role.
     * Moderator has permissions to manage the team, but not as much as the leader.
     */
    MODERATOR("moderator"),
    /**
     * Member role.
     */
    MEMBER("member");

    private final String roleName;
    private final List<String> permissions;

    /**
     * Instantiates a new Role.
     *
     * @param roleName the role name
     */
    Role(String roleName) {
        this.roleName = roleName;
        List<String> memberPermissions = List.of("lienubsteam.team.tpa", "lienubsteam.team.join.request");
        List<String> moderatorPermissions = new java.util.ArrayList<>(List.of("lienubsteam.team.invite.add", "lienubsteam.team.kick", "lienubsteam.team.claim", "lienubsteam.team.unclaim", "lienubsteam.team.join.accept", "lienubsteam.team.join.deny"));
        moderatorPermissions.addAll(memberPermissions);
        List<String> leaderPermissions = new java.util.ArrayList<>(List.of("lienubsteam.team.promote", "lienubsteam.team.demote", "lienubsteam.team.disband", "lienubsteam.team.transfer"));
        leaderPermissions.addAll(moderatorPermissions);

        switch (roleName) {
            case "leader":
                this.permissions = leaderPermissions;
                break;
            case "moderator":
                this.permissions = moderatorPermissions;
                break;
            case "member":
                this.permissions = memberPermissions;
                break;
            default:
                this.permissions = List.of("lienubsteam.team.info", "lienubsteam.team.create", "lienubsteam.team.join", "lienubsteam.team.leave", "lienubsteam.team.invite.accept", "lienubsteam.team.invite.deny");
                break;
        }
    }

    /**
     * Gets role name.
     *
     * @return the role name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Gets permissions of the role.
     *
     * @return the permissions
     */
    public List<String> getPermissions() {
        return permissions;
    }

    /**
     * From string role.
     * Converts a string to a Role enum, if it exists.
     *
     * @param role the role
     * @return the role
     * @throws IllegalArgumentException if the role does not exist
     */
    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.getRoleName().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Role.class.getCanonicalName() + "." + role);
    }
}
