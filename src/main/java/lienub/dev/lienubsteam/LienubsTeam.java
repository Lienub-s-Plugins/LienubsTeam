package lienub.dev.lienubsteam;

import lienub.dev.lienubsteam.listeners.PlayerPositionListener;
import lienub.dev.lienubsteam.utils.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LienubsTeam extends JavaPlugin {
    private static LienubsTeam instance;
    public Database database;

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

    @Override
    public void onEnable() {
        setInstance(this);
        onPluginStart();
    }

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

    private static void setInstance(@Nullable LienubsTeam pluginInstance) {
        instance = pluginInstance;
    }

    public static LienubsTeam getPlugin() {
        return instance;
    }

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

        logger.log(Level.INFO, "Registering listeners...");
        registerListener();

        logger.log(Level.INFO, "Registering commands...");
        getCommands();

        logger.log(Level.INFO, "Plugin started in " + (System.nanoTime() - timestamp) + " nanoseconds.");

    }

    public void getCommands() {
        //TODO: Register commands
    }

    public void registerListener() {
        new PlayerPositionListener(this);
    }
}
