package lienub.dev.lienubsteam.utils.team;

import java.util.List;

public enum Role {
    LEADER("leader"),
    MODERATOR("moderator"),
    MEMBER("member");

    private final String role;
    private final List<String> permissions;

    Role(String role) {
        this.role = role;
        List<String> memberPermissions = List.of("lienubsteam.team.tpa", "lienubsteam.team.join.request");
        List<String> moderatorPermissions = new java.util.ArrayList<>(List.of("lienubsteam.team.invite.add", "lienubsteam.team.kick", "lienubsteam.team.claim", "lienubsteam.team.unclaim", "lienubsteam.team.join.accept",  "lienubsteam.team.join.deny"));
        moderatorPermissions.addAll(memberPermissions);
        List<String> leaderPermissions = new java.util.ArrayList<>(List.of("lienubsteam.team.promote", "lienubsteam.team.demote", "lienubsteam.team.disband", "lienubsteam.team.transfer"));
        leaderPermissions.addAll(moderatorPermissions);

        switch (role) {
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

    public String getRole() {
        return role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.getRole().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Role.class.getCanonicalName() + "." + role);
    }
}
