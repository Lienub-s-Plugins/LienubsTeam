package lienub.dev.lienubsteam.utils.team;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Member {
    private final UUID uuid;
    private Role role;

    public Member(UUID player, @Nullable Role role) {
        this.uuid = player;
        this.role = role != null ? role : Role.MEMBER;
    }

    public UUID getPlayerUUID() {
        return uuid;
    }

    public String getRole() {
        return role.getRole();
    }

    public void setRole(String role) {
        this.role = Role.valueOf(role);
    }
}
