package lienub.dev.lienubsteam.utils.db;

import lienub.dev.lienubsteam.LienubsTeam;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class Database {
    String url = "jdbc:sqlite:";
    LienubsTeam plugin;
    Logger logger;

    public Database(LienubsTeam plugin) {
        this.plugin = plugin;
        logger = plugin.getLogger();
        init();
        initTables();
    }

    public void init() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            logger.severe("SQLite JDBC driver not found.");
            return;
        }

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs(); // create the plugin's data folder
        }

        //look for plugins folder
        File databaseFile = new File(dataFolder, "database.db");
        if (!databaseFile.exists()) {
            File parent = databaseFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            try {
                databaseFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.url += databaseFile.getAbsolutePath();

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                logger.info("The driver name is " + meta.getDriverName());
                logger.info("Database operational.");
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }

    public void makeQuery(@NotNull String query) {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }

    public List<String> selectQuery(@NotNull String query, List<Object> parameters) {
        List<String> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            ResultSet rs = pstmt.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return list;
    }

    public List<String> selectQuery(@NotNull String query) {
        return selectQuery(query, new ArrayList<>());
    }

    public void close() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }

    public void initTables() {
        makeQuery("""
                CREATE TABLE IF NOT EXISTS team (
                id integer PRIMARY KEY,
                	name text NOT NULL,
                	owner_id text NOT NULL,
                	created_at datetime DEFAULT CURRENT_TIMESTAMP,
                	updated_at datetime DEFAULT CURRENT_TIMESTAMP
                );""");
        makeQuery("""
                CREATE TABLE IF NOT EXISTS team_members (
                team_id integer NOT NULL,
                	player_id text NOT NULL,
                	role text NOT NULL,
                	created_at datetime DEFAULT CURRENT_TIMESTAMP,
                	updated_at datetime DEFAULT CURRENT_TIMESTAMP,
                 PRIMARY KEY (team_id, player_id),
                 FOREIGN KEY (team_id) REFERENCES team (id)
                 ON UPDATE CASCADE ON DELETE CASCADE
                );""");
        makeQuery("""
                CREATE TABLE IF NOT EXISTS claim_chunks (
                id integer PRIMARY KEY,
                	team_id integer NOT NULL,
                	x integer NOT NULL,
                	z integer NOT NULL,
                	world text NOT NULL,
                	created_at datetime DEFAULT CURRENT_TIMESTAMP,
                	updated_at datetime DEFAULT CURRENT_TIMESTAMP,
                 FOREIGN KEY (team_id) REFERENCES team (id)
                 ON UPDATE CASCADE ON DELETE CASCADE
                );""");
    }
}
