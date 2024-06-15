package lienub.dev.lienubsteam.utils.managers;

import lienub.dev.lienubsteam.LienubsTeam;
import lienub.dev.lienubsteam.utils.db.dao.TeamDAO;
import lienub.dev.lienubsteam.utils.team.Member;
import lienub.dev.lienubsteam.utils.team.Role;
import lienub.dev.lienubsteam.utils.team.Team;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
    private List<Team> teams;
    private TeamDAO teamDAO;

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
                if (LienubsTeam.getInstance().getServer().getOfflinePlayer(member.getPlayerUUID()).equals(player)) {
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
}
