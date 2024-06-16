package lienub.dev.lienubsteam.utils;

import lienub.dev.lienubsteam.LienubsTeam;
import lienub.dev.lienubsteam.utils.team.Team;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for managing claim boss bars.
 * This class is used to create and update boss bars for players when they enter a team's territory.
 *
 * @see LienubsTeam
 * @see Bukkit
 * @see Chunk
 * @see NamespacedKey
 * @see BarColor
 * @see BarStyle
 * @see KeyedBossBar
 * @see Player
 * @see Team
 *
 * @since 1.0.0
 * @version 1.0.0
 */
public class ClaimBossBarHolder {
    private static final LienubsTeam plugin = LienubsTeam.getInstance();
    private static final String TITLE_PREFIX = ChatColor.GOLD + ChatColor.BOLD.toString() + "TERRITOIRE : " + ChatColor.RESET;

    private ClaimBossBarHolder() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Sets claim boss bar.
     *
     * @param player the player
     * @param chunk  the chunk
     */
    public static void setupClaimBossBar(Player player, Chunk chunk) {
        String title = TITLE_PREFIX + getClaimingTeamName(chunk);

        NamespacedKey key = new NamespacedKey(plugin, "claimBossBar-" + player.getUniqueId());

        // If player already has a boss bar, with the same key, do not create a new one.
        if (Bukkit.getBossBar(key) != null) {
            return;
        }

        KeyedBossBar bossBar = Bukkit.createBossBar(key, title, BarColor.BLUE, BarStyle.SOLID);
        bossBar.addPlayer(player);
    }

    private static String getClaimingTeamName(Chunk chunk) {
        Team team = plugin.getTeamManager().getTeamByChunk(chunk);

        if (team == null) {
            return "Libre";
        } else {
            return team.getName();
        }
    }

    /**
     * Update boss bar.
     *
     * @param bossBar the boss bar
     * @param chunk   the chunk
     */
    public static void updateBossBar(@NotNull KeyedBossBar bossBar, Chunk chunk) {
        String title = TITLE_PREFIX + getClaimingTeamName(chunk);

        bossBar.setTitle(title);
    }
}
