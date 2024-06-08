package lienub.dev.lienubsteam.utils.team;

import lienub.dev.lienubsteam.utils.db.dao.TeamDAO;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;

public class Team {
    private final TeamDAO teamDAO;
    private Integer id;
    private String name;
    private String leader;
    private List<Member> members;
    private List<Chunk> claimedChunks;

    public Team(TeamDAO dao, Integer id, String name, String leader, List<Member> members, List<Chunk> claimedChunks) {
        this.teamDAO = dao;
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.members = members;
        this.claimedChunks = claimedChunks;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer i) {
        id = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public List<Member> getMembers() {
        return members;
    }
    public void addMember(Member member) {
        teamDAO.insert(this, member);
    }

    public void removeMember(Member member) {
        teamDAO.delete(this, member);
    }

    public List<Chunk> getClaimedChunks() {
        return claimedChunks;
    }

    public void addClaimedChunk(Chunk chunk) {
        teamDAO.insert(this, chunk);
    }

    public void removeClaimedChunk(Chunk chunk) {
        teamDAO.delete(this, chunk);
    }
}
