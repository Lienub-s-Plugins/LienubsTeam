package lienub.dev.lienubsteam;

import lienub.dev.lienubsteam.commands.TeamCommand;
import lienub.dev.lienubsteam.listeners.BlockInteractionListener;
import lienub.dev.lienubsteam.listeners.PlayerPositionListener;
import lienub.dev.lienubsteam.utils.db.Database;
import lienub.dev.lienubsteam.utils.managers.TeamManager;
import lienub.dev.lienubsteam.utils.team.Member;
import lienub.dev.lienubsteam.utils.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.Nullable;
import lienub.dev.lienubsteam.utils.db.dao.TeamDAO;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class describing the main plugin.
 *
 * @version 1.0
 * @see JavaPlugin
 * @see Plugin
 * @see PluginDescriptionFile
 * @see Database
 * @see TeamManager
 * @see UUID
 * @see PermissionAttachment
 * @see File
 * @see HashMap
 * @see Logger
 * @see TeamCommand
 * @see Player
 * @see Bukkit
 * @see PlayerPositionListener
 * @see BlockInteractionListener
 * @see TeamInfoListener
 * @see PlayerJoinListener
 * @see TeamDAO
 * @see ParametersAreNonnullByDefault
 * @see JavaPluginLoader
 * @see ParametersAreNonnullByDefault
 * @see ParametersAreNonnullByDefault
 * @see ParametersAreNonnullByDefault
 * @see #onEnable()
 * @see #onDisable()
 * @see #setInstance(LienubsTeam)
 * @see #getInstance()
 * @see #getTeamManager()
 * @see #getPlayerPermissions()
 * @see #onPluginStart()
 * @see #registerObjects()
 * @see #registerCommands()
 * @see #registerListener()
 * @see #LienubsTeam()
 * @see #LienubsTeam(JavaPluginLoader, PluginDescriptionFile, File, File)
 * @see #instance
 * @see #database
 * @see #teamManager
 * @see #playerPermissions
 * @since 1.0
 */
public final class LienubsTeam extends JavaPlugin {
    private static LienubsTeam instance;
    public Database database;
    public TeamManager teamManager;
    /**
     * Our default constructor for {@link LienubsTeam}.
     */
    public LienubsTeam() {
        super();
    }

    /**
     * This constructor is invoked in Unit Test environments only.
     *
     * @param loader      Our {@link JavaPluginLoader}
     * @param description A {@link PluginDescriptionFile}
     * @param dataFolder  The data folder
     * @param file        A {@link File} for this {@link Plugin}
     */
    @ParametersAreNonnullByDefault
    public LienubsTeam(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    /**
     * When the plugin is enabled.
     *
     * @see #onPluginStart()
     * @see #setInstance(LienubsTeam)
     * @since 1.0
     */
    @Override
    public void onEnable() {
        setInstance(this);
        onPluginStart();
    }

    /**
     * When the plugin is disabled.
     *
     * @see Bukkit
     * @see #instance
     * @see #setInstance(LienubsTeam)
     * @see Player
     * @see PermissionAttachment
     * @see Bukkit#getOnlinePlayers()
     * @see Player#closeInventory()
     * @see Player#removeAttachment(PermissionAttachment)
     * @since 1.0
     */
    @Override
    public void onDisable() {
        if (instance == null) {
            return;
        }

        // Cancel all tasks
        Bukkit.getScheduler().cancelTasks(this);

        // Terminate instance
        setInstance(null);

        // Close all inventories
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.closeInventory();
        }
    }

    /**
     * Sets instance of the plugin.
     *
     * @param pluginInstance the plugin instance
     * @see #instance
     * @since 1.0
     */
    private static void setInstance(@Nullable LienubsTeam pluginInstance) {
        instance = pluginInstance;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     * @see #instance
     * @see JavaPlugin#getPlugin(Class)
     * @since 1.0
     */
    public static LienubsTeam getInstance() {
        return getPlugin(LienubsTeam.class);
    }

    /**
     * Gets team manager.
     *
     * @return the team manager
     */
    public TeamManager getTeamManager() {
        return teamManager;
    }

    /**
     * Gets player permissions.
     *
     * @return the player permissions
     * @see UUID
     * @see PermissionAttachment
     */
    public Map<UUID, PermissionAttachment> getPlayerPermissions() {
        return playerPermissions;
    }

    /**
     * On plugin start.
     *
     * @see System
     * @see Logger
     * @see #getLogger()
     * @see Bukkit#getVersion()
     * @see Logger
     * @see #getDataFolder()
     * @see Database
     * @see #registerObjects()
     * @see #registerListener()
     * @see #registerCommands()
     * @since 1.0
     */
    public void onPluginStart() {
        long timestamp = System.nanoTime();
        Logger logger = getLogger();

        //Check Paper installation
        if (Bukkit.getVersion().contains("Paper")) {
            logger.info("Paper detected, continuing...");
        } else {
            logger.warning("Paper not detected, consider using it.");
        }

        //Initialize database
        //check if data folder exists
        if (!getDataFolder().exists()) {
            if (!getDataFolder().mkdir()) {
                logger.log(Level.SEVERE, "Could not create data folder.");
            }
        }

        database = new Database(this);

        //Check Java version
        String javaVersion = System.getProperty("java.version");
        if (javaVersion.contains("1.8") || javaVersion.contains("1.9") || javaVersion.contains("1.10") || javaVersion.contains("1.11") || javaVersion.contains("1.12") || javaVersion.contains("1.13") || javaVersion.contains("1.14") || javaVersion.contains("1.15") || javaVersion.contains("1.16")) {
            logger.warning("Java version is outdated, please update to Java 17 or higher.");
        } else {
            logger.info("Java version is up to date.");
        }

        logger.log(Level.INFO, "Initializing objects...");
        registerObjects();

        logger.log(Level.INFO, "Registering listeners...");
        registerListener();

        logger.log(Level.INFO, "Registering commands...");
        registerCommands();

        logger.log(Level.INFO, "Plugin started in " + (System.nanoTime() - timestamp) + " nanoseconds.");

    }

    /**
     * Register objects.
     *
     * @see TeamManager
     * @see TeamDAO
     * @see #database
     * @see #teamManager
     * @since 1.0
     */
    public void registerObjects() {
        teamManager = new TeamManager(new TeamDAO(database));
    }

    /**
     * Register commands.
     *
     * @see TeamCommand
     * @see #getCommand(String)
     * @since 1.0
     */
    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("team")).setExecutor(new TeamCommand(this));
    }

    /**
     * Register listener.
     *
     * @see PlayerPositionListener
     * @see BlockInteractionListener
     * @see TeamInfoListener
     * @see PlayerJoinListener
     * @since 1.0
     */
    public void registerListener() {
        new PlayerPositionListener(this);
        new BlockInteractionListener(this);
    }
}
