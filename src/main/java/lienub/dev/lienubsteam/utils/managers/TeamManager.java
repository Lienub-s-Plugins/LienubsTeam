package lienub.dev.lienubsteam.utils.managers;

import lienub.dev.lienubsteam.utils.db.dao.TeamDAO;
import lienub.dev.lienubsteam.utils.team.Member;
import lienub.dev.lienubsteam.utils.team.Role;
import lienub.dev.lienubsteam.utils.team.Team;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The class Team manager.
 * Manages all teams and their members.
 * Handles invitations, join requests and teleport requests.
 * Handles team creation, deletion and retrieval.
 * Handles player retrieval and role assignment.
 * Handles chunk retrieval and team assignment.
 * Handles player teleportation.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @see Team
 * @see Member
 * @see Role
 * @see TeamDAO
 * @see Player
 * @see Chunk
 * @see UUID
 * @see HashMap
 * @see ArrayList
 * @see List
 * @see Map
 * @see Optional
 * @see Arrays
 * @see Collections
 * @see HashSet
 * @see Iterator
 */
public class TeamManager {
    private final Map<UUID, List<Team>> invitations = new HashMap<>();
    private final Map<UUID, List<Team>> joinRequests = new HashMap<>();
    private final Map<UUID, UUID> tpaRequests = new HashMap<>();
    private final List<Team> teams;
    private final TeamDAO teamDAO;

    /**
     * Instantiates a new Team manager.
     *
     * @param teamDAO the team dao
     */
    public TeamManager(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
        this.teams = teamDAO.get();
    }

    /**
     * Create a team.
     * Adds the team to the list of teams.
     * Saves the team to the database.
     *
     * @param name   the name
     * @param leader the leader
     * @return the team
     *
     * @see Team
     * @see TeamDAO
     */
    public Team createTeam(String name, Player leader) {
        Team team = teamDAO.createTeam(name, leader);
        teams.add(team);

        return team;
    }

    /**
     * Gets invitations.
     *
     * @return the invitations
     */
    public Map<UUID, List<Team>> getInvitations() {
        return invitations;
    }

    /**
     * Gets tpa requests.
     *
     * @return the tpa requests
     */
    public Map<UUID, UUID> getTpaRequests() {
        return tpaRequests;
    }

    /**
     * Gets join requests.
     *
     * @return the join requests
     */
    public Map<UUID, List<Team>> getJoinRequests() {
        return joinRequests;
    }

    /**
     * Gets teams.
     *
     * @return the teams
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Gets claimed chunks.
     *
     * @return the claimed chunks
     */
    public List<Chunk> getClaimedChunks() {
        List<Chunk> chunks = new ArrayList<>();
        for (Team team : teams) {
            chunks.addAll(team.getClaimedChunks());
        }
        return chunks;
    }

    /**
     * Gets team.
     *
     * @param name the name
     * @return the team
     */
    public Team getTeam(String name) {
        for (Team team : teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }

    /**
     * Gets team from player.
     *
     * @param player the player
     * @return the team from player
     */
    public Team getTeamFromPlayer(Player player) {
        for (Team team : teams) {
            for (Member member : team.getMembers()) {
                if (member.getPlayerUUID().equals(player.getUniqueId())) {
                    return team;
                }
            }
        }
        return null;
    }

    /**
     * Gets team by chunk.
     *
     * @param chunk the chunk
     * @return the team by chunk
     */
    public Team getTeamByChunk(Chunk chunk) {
        for (Team team : teams) {
            if (team.getClaimedChunks().contains(chunk)) {
                return team;
            }
        }
        return null;
    }

    /**
     * Gets member.
     *
     * @param player the player
     * @return the member
     */
    public Member getMember(Player player) {
        for (Team team : teams) {
            for (Member member : team.getMembers()) {
                if (member.getPlayerUUID().equals(player.getUniqueId())) {
                    return member;
                }
            }
        }
        return null;
    }

    /**
     * Invite player to team.
     *
     * @param player the player
     * @param team   the team
     */
    public void invitePlayerToTeam(@NotNull Player player, Team team) {
        if (invitations.containsKey(player.getUniqueId())) {
            invitations.get(player.getUniqueId()).add(team);
        } else {
            invitations.put(player.getUniqueId(), new ArrayList<>(List.of(team)));
        }
    }

    /**
     * Request join team.
     *
     * @param player the player
     * @param team   the team
     */
    public void requestJoinTeam(@NotNull Player player, Team team) {
        if (joinRequests.containsKey(player.getUniqueId())) {
            joinRequests.get(player.getUniqueId()).add(team);
        } else {
            joinRequests.put(player.getUniqueId(), new ArrayList<>(List.of(team)));
        }
    }

    /**
     * Request teleport.
     *
     * @param sender the sender
     * @param target the target
     */
    public void requestTeleport(@NotNull Player sender, @NotNull Player target) {
        tpaRequests.put(target.getUniqueId(), sender.getUniqueId());
    }

    /**
     * Accept invitation.
     *
     * @param player the player
     * @param t      the t
     */
    public void acceptInvitation(@NotNull Player player, Team t) {
        Team team = invitations.get(player.getUniqueId()).contains(t) ? t : null;
        if (team != null) {
            // The player was invited, add them to the team
            team.addMember(new Member(player.getUniqueId(), Role.MEMBER));
            // Remove the invitations
            invitations.remove(player.getUniqueId()).remove(team);
        }
    }

    /**
     * Accept join request.
     *
     * @param player the player
     * @param t      the t
     */
    public void acceptJoinRequest(@NotNull Player player, Team t) {
        Team team = joinRequests.get(player.getUniqueId()).contains(t) ? t : null;
        if (team != null) {
            // The player was accepted, add them to the team
            team.addMember(new Member(player.getUniqueId(), Role.MEMBER));
            joinRequests.remove(player.getUniqueId()).remove(team);
        }
    }

    /**
     * Deny invitation.
     *
     * @param player the player
     * @param team   the team
     */
    public void denyInvitation(@NotNull Player player, Team team) {
        // Remove the invitation without adding the player to the team
        invitations.remove(player.getUniqueId()).remove(team);
    }

    /**
     * Deny join request.
     *
     * @param player the player
     * @param team   the team
     */
    public void denyJoinRequest(@NotNull Player player, Team team) {
        // Remove the request without adding the player to the team
        joinRequests.remove(player.getUniqueId()).remove(team);
    }

    /**
     * Disband team.
     *
     * @param team the team
     */
    public void disbandTeam(Team team) {
        teamDAO.delete(team);
        teams.remove(team);
    }
}
