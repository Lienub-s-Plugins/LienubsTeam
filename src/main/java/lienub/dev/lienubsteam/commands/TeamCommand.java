package lienub.dev.lienubsteam.commands;

import lienub.dev.lienubsteam.LienubsTeam;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import lienub.dev.lienubsteam.utils.team.Team;
import lienub.dev.lienubsteam.utils.TextComponentUtil;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import java.util.*;

/**
 * Commands to manage teams
 *
 * @version 1.0.0
 * @since 1.0.0
 * @see CommandExecutor
 * @see TabExecutor
 * @see Command
 * @see CommandSender
 */
public class TeamCommand implements CommandExecutor, TabExecutor {

    private final LienubsTeam plugin;

    public TeamCommand(LienubsTeam plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("team").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            return false;
        } else if (strings.length == 1) {
            switch (strings[0]) {
                case "info":
                    info(commandSender);
                    break;
                case "claim":
                    claim(commandSender);
                    break;
                case "unclaim":
                    unclaim(commandSender);
                    break;
                default:
                    return false;
            }
        } else if (strings.length == 2) {
            switch (strings[0]) {
                case "create":
                    create(commandSender, strings);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return Arrays.asList("info", "create", "claim", "unclaim");
        }
        return new ArrayList<>();
    }

    private void info(CommandSender commandSender) {
        // Get the team the player is in and send the team info to the player
        Team team = plugin.teamManager.getTeamFromPlayer((Player) commandSender);
        if (team == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("You are not in a team."));
            return;
        }

        commandSender.spigot().sendMessage(TextComponentUtil.createInfoMessage("Team Info"));
        commandSender.sendMessage("Name: " + team.getName());
        commandSender.sendMessage("Leader: " + plugin.getServer().getPlayer(UUID.fromString(team.getLeader())).getName());
        commandSender.sendMessage("Members: ");
        for (int i = 0; i < team.getMembers().size(); i++) {
            commandSender.sendMessage(plugin.getServer().getOfflinePlayer(team.getMembers().get(i).getPlayerUUID()).getName());
        }

    }
    private void create(CommandSender commandSender, String[] strings) {
        if (plugin.teamManager.getTeamFromPlayer((Player) commandSender) != null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("You are already in a team."));
            return;
        }
        // Create a team with the name of the first argument and the player as the leader, add that to the database
        String team_name = strings[1];
        commandSender.spigot().sendMessage(TextComponentUtil.createInfoMessage("Creating team " + team_name + "..."));
        Team res = plugin.teamManager.createTeam(team_name, (Player) commandSender);
        commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Team " + res.getName() + " created!"));

    }
    private void claim(CommandSender commandSender) {
        // Claim the chunk the player is standing in
        //If player is not in a team, return false
        Team team = plugin.teamManager.getTeamFromPlayer((Player) commandSender);

        if (team == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("You are not in a team."));
            return;
        }

        //If the chunk is already claimed by any team, return false
        if (plugin.teamManager.getClaimedChunks().contains(((Player) commandSender).getLocation().getChunk())) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("This chunk is already claimed."));
            return;
        }

        //Claim the chunk
        team.addClaimedChunk(((Player) commandSender).getLocation().getChunk());
        commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Chunk claimed!"));
    }
    private void unclaim(CommandSender commandSender) {
        // Claim the chunk the player is standing in
        //If player is not in a team, return false
        Team team = plugin.teamManager.getTeamFromPlayer((Player) commandSender);

        if (team == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("You are not in a team."));
            return;
        }

        //If the chunk is already claimed by any team, return false
        if (!plugin.teamManager.getClaimedChunks().contains(((Player) commandSender).getLocation().getChunk())) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("This chunk is not claimed."));
            return;
        }

        //Unclaim the chunk
        team.removeClaimedChunk(((Player) commandSender).getLocation().getChunk());
        commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Chunk unclaimed!"));
    }
}
