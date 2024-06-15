package lienub.dev.lienubsteam.utils.team;

import lienub.dev.lienubsteam.utils.db.dao.TeamDAO;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The class representing a team in the plugin.
 *
 * @version 1.0
 * @see Member
 * @see Chunk
 * @see TeamDAO
 * @since 1.0
 */
public class Team {
    private final TeamDAO teamDAO;
    private Integer id;
    private String name;
    private String leader;
    private List<Member> members;
    private List<Chunk> claimedChunks;

    /**
     * Instantiates a new Team.
     *
     * @param dao           the dao
     * @param id            the id
     * @param name          the name
     * @param leader        the leader
     * @param members       the members
     * @param claimedChunks the claimed chunks
     */
    public Team(TeamDAO dao, Integer id, String name, UUID leader, List<Member> members, List<Chunk> claimedChunks) {
        this.teamDAO = dao;
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.members = members;
        this.claimedChunks = claimedChunks;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param i the id
     */
    public void setId(Integer i) {
        id = i;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    /**
     * Gets members.
     *
     * @return the members
     */
    public List<Member> getMembers() {
        return members;
    }

    /**
     * Add member.
     *
     * @param member the member
     */
    public void addMember(Member member) {
        teamDAO.insert(this, member);
    }

    /**
     * Remove member.
     *
     * @param member the member
     */
    public void removeMember(Member member) {
        teamDAO.delete(this, member);
    }

    public List<Chunk> getClaimedChunks() {
        return claimedChunks;
    }

    /**
     * Add claimed chunk.
     *
     * @param chunk the chunk
     */
    public void addClaimedChunk(Chunk chunk) {
        teamDAO.insert(this, chunk);
    }

    /**
     * Remove claimed chunk.
     *
     * @param chunk the chunk
     */
    public void removeClaimedChunk(Chunk chunk) {
        teamDAO.delete(this, chunk);
    }
}
