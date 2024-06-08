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

public class TeamManager {
    private List<Team> teams;
    private TeamDAO teamDAO;

    public TeamManager(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
        this.teams = teamDAO.get();
    }

    public Team createTeam(String name, Player leader) {
        Team team = teamDAO.createTeam(name, leader);
        teams.add(team);

        return team;
    }

    public void addTeam(Team team) {
        teams.add(teamDAO.insert(team));
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Chunk> getClaimedChunks() {
        List<Chunk> chunks = new ArrayList<>();
        for (Team team : teams) {
            chunks.addAll(team.getClaimedChunks());
        }
        return chunks;
    }

    public Team getTeam(String name) {
        for (Team team : teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }

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

    public Team getTeamByChunk(Chunk chunk) {
        for (Team team : teams) {
            if (team.getClaimedChunks().contains(chunk)) {
                return team;
            }
        }
        return null;
    }
}
