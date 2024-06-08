package lienub.dev.lienubsteam.utils.db.dao;

import lienub.dev.lienubsteam.LienubsTeam;
import lienub.dev.lienubsteam.utils.db.Database;
import lienub.dev.lienubsteam.utils.team.Member;
import lienub.dev.lienubsteam.utils.team.Role;
import lienub.dev.lienubsteam.utils.team.Team;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeamDAO implements DAO<Team, Object> {
    private final Database database;

    public TeamDAO(Database database) {
        this.database = database;
    }


    public Team createTeam(String name, Player leader) {
        List<Member> members = new ArrayList<>();
        members.add(new Member(leader.getUniqueId(), Role.LEADER));
        Team team = new Team(this, null, name, leader.getUniqueId().toString(), members, new ArrayList<>());
        return insert(team);
    }

    @Override
    public Team insert(Team in) {
        try {
            String query = "INSERT INTO team (name, owner_id) VALUES (?, ?)";
            List<Object> params = List.of(in.getName(), in.getLeader());
            database.makeQuery(query, params);

        } catch (Exception e) {
            LienubsTeam.getInstance().getLogger().severe("Failed to insert team into database.");
        }

        // Get the team after inserting it into the database
        try {
            String query = "SELECT id FROM team WHERE name = ? AND owner_id = ?";
            List<Map<String, Object>> results = database.selectQuery(query, List.of(in.getName(), in.getLeader()));
            for (Map<String, Object> result : results) {
                in.setId((Integer) result.get("id"));
            }
        } catch (Exception e) {
            LienubsTeam.getInstance().getLogger().severe("Failed to get team from database: " + e.getMessage());
        }

        List<Member> membersCopy = new ArrayList<>(in.getMembers());
        for (Member member : membersCopy) {
            in.addMember(member);
        }

        List<Chunk> chunksCopy = new ArrayList<>(in.getClaimedChunks());
        for (Chunk chunk : chunksCopy) {
            in.addClaimedChunk(chunk);
        }

        return in;
    }

    @Override
    public void insert(Team in, Object object) {
        try {
            String query;
            List<Object> params;
            LienubsTeam.getInstance().getLogger().warning("CLASSNAME: " + object.getClass().getSimpleName());
            switch (object.getClass().getSimpleName()) {
                case "Member":
                    Member player = (Member) object;

                    query = "INSERT INTO team_members (team_id, player_id, role) VALUES (?, ?, ?)";
                    params = List.of(in.getId(), player.getPlayerUUID().toString(), player.getRole());
                    LienubsTeam.getInstance().getLogger().info("params: " + params);
                    database.makeQuery(query, params);
                    in.getMembers().add(player);
                    break;
                case "CraftChunk":
                    Chunk chunk = (Chunk) object;
                    query = "INSERT INTO claim_chunks (team_id, x, z, world) VALUES (?, ?, ?, ?)";
                    params = List.of(in.getId(), chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
                    database.makeQuery(query, params);
                    in.getClaimedChunks().add(chunk);
                    break;
                default:
                    LienubsTeam.getInstance().getLogger().severe("Failed to insert object: " + object + " into database.");
            }
        } catch (Exception e) {
            LienubsTeam.getInstance().getLogger().severe("Failed to insert object: " + object + " into database: " + e.getMessage());
        }
    }

    @Override
    public void update(Team in, Object object) {

    }

    @Override
    public void update(Team in) {
    }

    @Override
    public void delete(Team in) {

    }

    @Override
    public void delete(Team in, Object object) {
        try {
            String query;
            List<Object> params;
            switch (object.getClass().getSimpleName()) {
                case "Member":
                    Member player = (Member) object;
                    query = "DELETE FROM team_members WHERE team_id = ? AND player_id = ?";
                    params = List.of(in.getId(), player.getPlayerUUID().toString());
                    database.makeQuery(query, params);
                    in.getMembers().remove(player);
                    break;
                case "CraftChunk":
                    Chunk chunk = (Chunk) object;
                    query = "DELETE FROM claim_chunks WHERE team_id = ? AND x = ? AND z = ? AND world = ?";
                    params = List.of(in.getId(), chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
                    database.makeQuery(query, params);
                    in.getClaimedChunks().remove(chunk);
                    break;
            }
        } catch (Exception e) {
            LienubsTeam.getInstance().getLogger().severe("Failed to delete object: " + object + " from database.");
        }
    }

    @Override
    public List<Team> get() {
        List<Team> teams = new ArrayList<>(List.of());
        List<Member> members = new ArrayList<>(List.of());
        List<Chunk> claimedChunks = new ArrayList<>(List.of());
        // get every team from the database
        try {

            // Get all teams
            String query = "SELECT * FROM team";
            List<Map<String, Object>> results = database.selectQuery(query);
            for (Map<String, Object> result : results) {
                Integer id = (Integer) result.get("id");
                String name = (String) result.get("name");
                String leader = (String) result.get("owner_id");
                Team team = new Team(this, id, name, leader, members, claimedChunks);
                teams.add(team);
            }

            // Add members to each team
            for (Team team : teams) {
                try {
                    query = "SELECT * FROM team_members WHERE team_id = ?";
                    List<Map<String, Object>> memberResults = database.selectQuery(query, List.of(team.getId()));
                    // Get all members of the team from uuid string stored in the database
                    for (Map<String, Object> memberResult : memberResults) {
                        LienubsTeam.getInstance().getLogger().info("MemberResult: " + memberResult);
                        String uuid = (String) memberResult.get("player_id");
                        Member member = new Member(UUID.fromString(uuid), Role.fromString((String) memberResult.get("role")));
                        team.getMembers().add(member);
                    }
                } catch (Exception e) {
                    LienubsTeam.getInstance().getLogger().severe("Failed to get members from database: " + e.getMessage());
                }

                // Add claimed chunks to each team
                try {
                    query = "SELECT * FROM claim_chunks WHERE team_id = ?";
                    List<Map<String, Object>> chunkResults = database.selectQuery(query, List.of(team.getId()));
                    // Get all claimed chunks of the team from x and z coordinates stored in the database
                    for (Map<String, Object> chunkResult : chunkResults) {
                        int x = (int) chunkResult.get("x");
                        int z = (int) chunkResult.get("z");
                        String world = (String) chunkResult.get("world");
                        Chunk chunk = LienubsTeam.getInstance().getServer().getWorld(world).getChunkAt(x, z);
                        team.getClaimedChunks().add(chunk);
                    }
                } catch (Exception e) {
                    LienubsTeam.getInstance().getLogger().severe("Failed to get claimed chunks from database.");
                }

            }
        } catch (Exception e) {
            LienubsTeam.getInstance().getLogger().severe("Failed to get teams from database.");
        }

        if (teams.isEmpty()) {
            LienubsTeam.getInstance().getLogger().warning("No teams found in database.");
        }

        return teams;
    }

    @Override
    public Team get(Object object) {
        return null;
    }
}
