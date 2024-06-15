package lienub.dev.lienubsteam.utils.db;

import lienub.dev.lienubsteam.LienubsTeam;
import org.jetbrains.annotations.NotNull;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;


/**
 * The class describing the database used by the plugin.
 *
 * @version 1.0
 * @see HikariDataSource
 * @see HikariConfig
 * @see Logger
 * @see File
 * @see SQLException
 * @see DriverManager
 * @see ResultSet
 * @see ResultSetMetaData
 * @see PreparedStatement
 * @see Connection
 * @see HashMap
 * @see ArrayList
 * @see List
 * @see Map
 * @see Objects
 * @see Optional
 * @see LienubsTeam
 * @see #initDatabase()
 * @see #initTables()
 * @see #makeQuery(String)
 * @see #makeQuery(String, List)
 * @see #selectQuery(String, List)
 * @see #selectQuery(String)
 * @see #checkJDBC()
 * @see #initDatabaseFile()
 * @see #setupDatabaseConnection()
 * @see #Database(LienubsTeam)
 * @see #url
 * @see #plugin
 * @see #logger
 * @see #ds
 * @since 1.0
 */
public class Database {
    private HikariDataSource ds;
    private String url = "jdbc:sqlite:";
    private LienubsTeam plugin;
    private Logger logger;

    /**
     * Instantiates a new Database.
     *
     * @param plugin the plugin instance linked to the database
     * @see LienubsTeam
     * @see Logger
     * @since 1.0
     */
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

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setDriverClassName("org.sqlite.JDBC");
        ds = new HikariDataSource(config);

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

    /**
     * Make a query using a safe prepared statement.
     * It does not include any parameters to be used in the query.
     *
     * @param query the query
     * @see #makeQuery(String, List)
     * @since 1.0
     */
    public void makeQuery(@NotNull String query) {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * Make query using a safe prepared statement.
     * It also includes the parameters to be used in the query.
     *
     * @param query      the query
     * @param parameters the parameters
     */
    public void makeQuery(@NotNull String query, List<Object> parameters) {
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * Select query list using a safe prepared statement.
     * It includes the parameters to be used in the query.
     *
     * @param query      the query
     * @param parameters the parameters
     * @return the list
     */
    public List<Map<String, Object>> selectQuery(@NotNull String query, List<Object> parameters) {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String colName = rsmd.getColumnName(i);
                    Object colVal = rs.getObject(i);
                    row.put(colName, colVal);
                }
                list.add(row);
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return list;
    }

    /**
     * Select query list using a safe prepared statement.
     * It does not include any parameters to be used in the query.
     *
     * @param query the query
     * @return the list
     * @see #selectQuery(String, List)
     */
    public List<Map<String, Object>> selectQuery(@NotNull String query) {
        return selectQuery(query, new ArrayList<>());
    }

    public void close() {
        if (ds != null) {
            ds.close();
        }
    }

    public void initTables() {
        makeQuery("""
                CREATE TABLE IF NOT EXISTS team (
                id integer PRIMARY KEY,
                	name text NOT NULL UNIQUE,
                	owner_id text NOT NULL UNIQUE,
                	created_at datetime DEFAULT CURRENT_TIMESTAMP,
                	updated_at datetime DEFAULT CURRENT_TIMESTAMP
                );""");
        makeQuery("""
                CREATE TABLE IF NOT EXISTS team_members (
                team_id integer NOT NULL,
                	player_id text NOT NULL UNIQUE,
                	role text NOT NULL DEFAULT 'member',
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
