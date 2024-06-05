package lienub.dev.lienubsteam;

import org.bukkit.plugin.java.JavaPlugin;

public final class LienubsTeam extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("onEnable is called!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
