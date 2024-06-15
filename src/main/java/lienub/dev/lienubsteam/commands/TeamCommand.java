package lienub.dev.lienubsteam.commands;

import lienub.dev.lienubsteam.LienubsTeam;
import lienub.dev.lienubsteam.holders.TeamInfoHolder;
import lienub.dev.lienubsteam.utils.team.Member;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import lienub.dev.lienubsteam.utils.team.Team;
import lienub.dev.lienubsteam.utils.TextComponentUtil;
import org.jetbrains.annotations.Nullable;

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
    private static final String NO_PERMISSION = "Tu n'as pas la permission de faire ça.";
    private static final String INVITE = "invite";
    private static final String PROMOTE = "promote";
    private static final String DEMOTE = "demote";
    private static final String ACCEPT = "accept";
    private static final String TRANSFER = "transfer";
    private static final String NO_TEAM = "Tu n'es pas dans une équipe.";
    private static final String MEMBER = "member";
    private static final String NOT_CONNECTED = "Ce joueur n'est pas connecté.";
    private static final String NOT_IN_TEAM = "Ce joueur n'est pas dans l'équipe.";
    private static final String MODERATOR = "moderator";
    private static final String LEADER = "leader";

    private final LienubsTeam plugin;

    /**
     * Instantiates a new Team command.
     *
     * @param plugin the plugin
     */
    public TeamCommand(LienubsTeam plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(this.plugin.getCommand("team")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 0) {
            return false;
        } else if (strings.length == 1) {
            return commandsWithoutArgs(commandSender, strings);
        } else if (strings.length == 2) {
            return commandsWithOneArg(commandSender, strings);
        } else if (strings.length == 3) {
            return commandsWithTwoArgs(commandSender, strings);
        }
        return false;
    }

    /**
     * Commands without arguments.
     *
     * @param commandSender the command sender
     * @param strings       the strings
     * @return a boolean indicating if the command was executed successfully
     */
    public boolean commandsWithoutArgs(CommandSender commandSender, String @NotNull [] strings) {
        switch (strings[0]) {
            case "info":
                if (!commandSender.hasPermission("lienubsteam.team.info")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                }
                info(commandSender);
                break;
            case "claim":
                if (!commandSender.hasPermission("lienubsteam.team.claim")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                claim(commandSender);
                break;
            case "unclaim":
                if (!commandSender.hasPermission("lienubsteam.team.unclaim")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                unclaim(commandSender);
                break;
            case "leave":
                if (!commandSender.hasPermission("lienubsteam.team.leave")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                leave(commandSender);
                break;
            case "disband":
                if (!commandSender.hasPermission("lienubsteam.team.disband")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                disband(commandSender);
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Commands with one argument.
     *
     * @param commandSender the command sender
     * @param strings       the strings
     * @return a boolean indicating if the command was executed successfully
     */
    public boolean commandsWithOneArg(CommandSender commandSender, String @NotNull [] strings) {
        switch (strings[0]) {
            case "create":
                if (!commandSender.hasPermission("lienubsteam.team.create")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                create(commandSender, strings);
                break;
            case "tpa":
                tpa(commandSender, strings);
                break;
            case INVITE:
                if (!commandSender.hasPermission("lienubsteam.team.invite.add")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                invite(commandSender, strings);
                break;
            case "join":
                if (!commandSender.hasPermission("lienubsteam.team.join.request")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                join(commandSender, strings);
                break;
            case "kick":
                if (!commandSender.hasPermission("lienubsteam.team.kick")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                kick(commandSender, strings);
                break;
            case PROMOTE:
                if (!commandSender.hasPermission("lienubsteam.team.promote")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                promote(commandSender, strings);
                break;
            case DEMOTE:
                if (!commandSender.hasPermission("lienubsteam.team.demote")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                demote(commandSender, strings);
                break;
            case TRANSFER:
                if (!commandSender.hasPermission("lienubsteam.team.transfer")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                transfer(commandSender, strings);
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Commands with two arguments.
     *
     * @param commandSender the command sender
     * @param strings       the strings
     * @return a boolean indicating if the command was executed successfully
     */
    public boolean commandsWithTwoArgs(CommandSender commandSender, String @NotNull [] strings) {
        switch (strings[0]) {
            case INVITE:
                if (!commandSender.hasPermission("lienubsteam.team.invite.accept") && !commandSender.hasPermission("lienubsteam.team.invite.deny")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                invite(commandSender, strings);
                break;
            case "join":
                if (!commandSender.hasPermission("lienubsteam.team.join.accept") && !commandSender.hasPermission("lienubsteam.team.join.deny")) {
                    commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
                    return true;
                }
                join(commandSender, strings);
                break;
            default:
                return false;
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 1) {
            return Arrays.asList("info", "create", "claim", "unclaim", INVITE, "join", "kick", PROMOTE, DEMOTE, "leave", "disband");
        } else if (strings.length == 2) {
            return completeWithOneArg(commandSender, strings);
        } else if (strings.length == 3) {
            return completeWithTwoArgs(commandSender, strings);
        }
        return new ArrayList<>();
    }

    private List<String> completeWithOneArg(@NotNull CommandSender commandSender, @NotNull String @NotNull [] strings) {
        switch (strings[0]) {
            case INVITE, "tpa", "join" -> {
                List<String> args = new ArrayList<>();
                args.add(ACCEPT);
                args.add("deny");
                if (strings[0].equals("tpa") || strings[0].equals(INVITE)) {
                    plugin.getServer().getOnlinePlayers().forEach(player -> args.add(player.getName()));
                }
                if (strings[0].equals("join") && strings[1].equals(ACCEPT) || strings[1].equals("deny")) {
                    plugin.getTeamManager().getJoinRequests().forEach((uuid, team) -> args.add(plugin.getServer().getOfflinePlayer(uuid).getName()));
                } else if (strings[0].equals("join")) {
                    plugin.getTeamManager().getTeams().forEach(team -> args.add(team.getName()));
                }
                if (strings[0].equals(INVITE) && strings[1].equals(ACCEPT) || strings[1].equals("deny")) {
                    plugin.getServer().getOnlinePlayers().forEach(player -> args.add(player.getName()));
                }
                return args;
            }
            case "kick", DEMOTE, PROMOTE, TRANSFER -> {
                Team team = plugin.getTeamManager().getTeamFromPlayer((Player) commandSender);
                if (team == null) {
                    return new ArrayList<>();
                }
                return team.getMembers().stream().map(member -> plugin.getServer().getOfflinePlayer(member.getPlayerUUID()).getName()).toList();
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    private List<String> completeWithTwoArgs(@NotNull CommandSender commandSender, @NotNull String @NotNull [] strings) {
        return switch (strings[0]) {
            case INVITE -> {
                if (strings[1].equals(ACCEPT) || strings[1].equals("deny")) {
                    yield plugin.getTeamManager().getInvitations().get(((Player) commandSender).getUniqueId()).stream().map(Team::getName).toList();
                }
                yield new ArrayList<>();
            }
            case "join" -> {
                if (strings[1].equals(ACCEPT) || strings[1].equals("deny")) {
                    yield plugin.getTeamManager().getJoinRequests().get(((Player) commandSender).getUniqueId()).stream().map(Team::getName).toList();
                }
                yield new ArrayList<>();
            }
            default -> new ArrayList<>();
        };
    }

    private void info(CommandSender commandSender) {
        // Get the team the player is in and send the team info to the player
        Player player = (Player) commandSender;
        Team team = plugin.getTeamManager().getTeamFromPlayer(player);
        if (team == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
            return;
        }

        TeamInfoHolder menu = new TeamInfoHolder(player);
        player.openInventory(menu.getInventory());


    }

    private void leave(CommandSender commandSender) {
        // Leave the team the player is in
        Team team = plugin.getTeamManager().getTeamFromPlayer((Player) commandSender);
        if (team == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
            return;
        }

        //If player is the leader, return false
        if (team.getLeader().equals(((Player) commandSender).getUniqueId())) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu ne peux pas quitter l'équipe en étant le chef."));
            //suggest to disband the team
            EnumMap<ClickEvent.Action, String> clickEvent = new EnumMap<>(ClickEvent.Action.class);
            clickEvent.put(ClickEvent.Action.SUGGEST_COMMAND, "/team disband");
            commandSender.spigot().sendMessage(TextComponentUtil.createSpecialMessage("Si tu veux dissoudre l'équipe, utilise /team disband", List.of(clickEvent)));
            //suggest to transfer the leadership
            clickEvent.put(ClickEvent.Action.SUGGEST_COMMAND, "/team transfer <joueur>");
            commandSender.spigot().sendMessage(TextComponentUtil.createSpecialMessage("Si tu veux transférer le leadership, utilise /team transfer <joueur>", List.of(clickEvent)));
            return;
        }

        Member member = team.getMembers().stream().filter(m -> m.getPlayerUUID().equals(((Player) commandSender).getUniqueId())).findFirst().orElse(null);
        team.removeMember(member);
        commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Tu as quitté l'équipe."));
    }

    private void join(CommandSender commandSender, String @NotNull [] strings) {
        // Join a team

        if (strings[1].equalsIgnoreCase(ACCEPT)) {
            acceptJoin(commandSender, strings);
        } else if (strings[1].equalsIgnoreCase("deny")) {
            denyJoin(commandSender, strings);
        } else {
            if (plugin.getTeamManager().getTeamFromPlayer((Player) commandSender) != null) {
                commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu es déjà dans une équipe."));
                return;
            }

            //If the team does not exist, return false
            Team team = plugin.getTeamManager().getTeam(strings[1]);
            if (team == null) {
                commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Cette équipe n'existe pas."));
                return;
            }

            plugin.getTeamManager().requestJoinTeam((Player) commandSender, team);

            // Send a notification to the team leader and moderators
            team.getMembers().forEach(member -> {
                Player player = plugin.getServer().getPlayer(member.getPlayerUUID());
                if (player != null && !Objects.equals(member.getRole(), MEMBER)) {
                    player.spigot().sendMessage(TextComponentUtil.createInfoMessage(commandSender.getName() + " veut rejoindre l'équipe. Acceptes-tu ?"));
                    player.spigot().sendMessage(TextComponentUtil.createAcceptDenyMessage("/team join accept " + commandSender.getName(), "/team join deny " + commandSender.getName()));
                }
            });
            commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Demande de rejoindre l'équipe envoyée."));
        }
    }

    private void acceptJoin(@NotNull CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission("lienubsteam.team.join.accept")) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
            return;
        }

        if (strings.length < 3) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Utilisation: /team join accept <joueur>"));
            return;
        }

        Player joiner = plugin.getServer().getPlayer(strings[2]);

        if (joiner == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NOT_CONNECTED));
            return;
        }

        if (plugin.getTeamManager().getJoinRequests().containsKey(joiner.getUniqueId())) {
            plugin.getTeamManager().acceptJoinRequest(joiner, plugin.getTeamManager().getTeamFromPlayer((Player) commandSender));
            commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Invitation acceptée !"));
        } else {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Ce joueur n'a pas demandé à rejoindre l'équipe."));
        }
    }

    private void denyJoin(@NotNull CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission("lienubsteam.team.join.deny")) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
            return;
        }

        if (strings.length < 3) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Utilisation: /team join deny <joueur>"));
            return;
        }

        Player joiner = plugin.getServer().getPlayer(strings[2]);

        if (joiner == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NOT_CONNECTED));
            return;
        }

        //Deny the invite
        if (plugin.getTeamManager().getJoinRequests().containsKey(joiner.getUniqueId())) {
            plugin.getTeamManager().denyJoinRequest(joiner, plugin.getTeamManager().getTeamFromPlayer((Player) commandSender));
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Invitation refusée !"));
        } else {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Ce joueur n'a pas demandé à rejoindre l'équipe."));
        }
    }

    private void create(CommandSender commandSender, String[] strings) {
        if (plugin.getTeamManager().getTeamFromPlayer((Player) commandSender) != null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu es déjà dans une équipe."));
            return;
        }

        if (plugin.getTeamManager().getTeams().stream().anyMatch(team -> team.getName().equals(strings[1]))) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Ce nom d'équipe est déjà pris."));
            return;
        }

        // Create a team with the name of the first argument and the player as the leader, add that to the database
        String teamName = strings[1];
        commandSender.spigot().sendMessage(TextComponentUtil.createInfoMessage("Creating team " + teamName + "..."));
        Team res = plugin.getTeamManager().createTeam(teamName, (Player) commandSender);
        commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Team " + res.getName() + " créée !"));

    }

    private void claim(CommandSender commandSender) {
        // Claim the chunk the player is standing in
        //If player is not in a team, return false
        Team team = plugin.getTeamManager().getTeamFromPlayer((Player) commandSender);

        if (team == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
            return;
        }

        //If the chunk is already claimed by any team, return false
        if (plugin.getTeamManager().getClaimedChunks().contains(((Player) commandSender).getLocation().getChunk())) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Ce chunk est déjà claim."));
            return;
        }

        //Claim the chunk
        team.addClaimedChunk(((Player) commandSender).getLocation().getChunk());
        commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Chunk claim !"));
    }

    private void unclaim(CommandSender commandSender) {
        // Claim the chunk the player is standing in
        //If player is not in a team, return false
        Team team = plugin.getTeamManager().getTeamFromPlayer((Player) commandSender);

        if (team == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
            return;
        }

        //If the chunk is already claimed by any team, return false
        if (!plugin.getTeamManager().getClaimedChunks().contains(((Player) commandSender).getLocation().getChunk())) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Ce chunk est déjà claim."));
            return;
        }

        //Unclaim the chunk
        team.removeClaimedChunk(((Player) commandSender).getLocation().getChunk());
        commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Chunk unclaim !"));
    }

    private void invite(CommandSender commandSender, String @NotNull [] strings) {
        // Invite a player to the team
        //If player is not in a team, return false
        Team team = plugin.getTeamManager().getTeamFromPlayer((Player) commandSender);

        if (strings[1].equalsIgnoreCase(ACCEPT) && !acceptInvite(commandSender, strings) || strings[1].equalsIgnoreCase("deny") && !denyInvite(commandSender, strings)) {
            return;
        }

        if (!commandSender.hasPermission("lienubsteam.team.invite.add")) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
            return;
        }

        if (team == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
            return;
        }

        //If the player is not online, return false
        Player invited = plugin.getServer().getPlayer(strings[1]);

        // if invited player is the inviter, return false
        if (invited == commandSender) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu ne peux pas t'inviter toi-même."));
            return;
        }

        if (invited == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NOT_CONNECTED));
            return;
        }

        //If the player is already in a team, return false
        if (plugin.getTeamManager().getTeamFromPlayer(invited) != null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Ce joueur est déjà dans une équipe"));
            return;
        }

        //If the player is already invited to a team, return false
        if (plugin.getTeamManager().getInvitations().containsKey(invited.getUniqueId())) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Ce joueur a déjà reçu une invitation."));
            return;
        }

        //Invite the player
        plugin.getTeamManager().invitePlayerToTeam(invited, team);
        invited.spigot().sendMessage(TextComponentUtil.createInfoMessage(commandSender.getName() + " t'a invité à rejoindre l'équipe " + team.getName() + " !"));
        invited.spigot().sendMessage(TextComponentUtil.createAcceptDenyMessage("/team invite accept " + team.getName(), "/team invite deny " + team.getName()));

        commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Joueur invité !"));
    }

    private boolean acceptInvite(@NotNull CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission("lienubsteam.team.invite.accept")) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
            return false;
        }

        if (strings.length < 3) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Utilisation: /team invite accept <team>"));
            return false;
        }

        if (plugin.getTeamManager().getInvitations().containsKey(((Player) commandSender).getUniqueId())) {
            // get team of the commander sender inside the invitation map
            Team invitedTeam = getInvitedTeam(commandSender, strings);

            if (invitedTeam == null) {
                return false;
            }

            plugin.getTeamManager().acceptInvitation((Player) commandSender, invitedTeam);
            commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Invitation acceptée !"));
            // Broadcast the online members of the team
            invitedTeam.getMembers().forEach(member -> {
                Player player = plugin.getServer().getPlayer(member.getPlayerUUID());
                if (player != null) {
                    player.spigot().sendMessage(TextComponentUtil.createSuccessMessage(commandSender.getName() + " a rejoint l'équipe !"));
                }
            });
        } else {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu n'as pas reçu d'invitation."));
            return false;
        }
        return true;
    }

    private boolean denyInvite(@NotNull CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission("lienubsteam.team.invite.deny")) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_PERMISSION));
            return false;
        }

        if (strings.length < 3) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Utilisation: /team invite deny <team>"));
            return false;
        }

        if (plugin.getTeamManager().getInvitations().containsKey(((Player) commandSender).getUniqueId())) {
            // get team of the commander sender inside the invitation map
            Team invitedTeam = getInvitedTeam(commandSender, strings);

            if (invitedTeam == null) {
                return false;
            }

            plugin.getTeamManager().denyInvitation((Player) commandSender, invitedTeam);
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Invitation refusée !"));
        } else {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu n'as pas reçu d'invitation."));
            return false;
        }
        return true;
    }

    @Nullable
    private Team getInvitedTeam(@NotNull CommandSender commandSender, String[] strings) {
        List<Team> invitedTeams = plugin.getTeamManager().getInvitations().get(((Player) commandSender).getUniqueId());
        Team invitedTeam = invitedTeams.stream().filter(t -> t.getName().equals(strings[2])).findFirst().orElse(null);

        if (invitedTeam == null) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu n'as pas reçu d'invitation de cette équipe."));
            return null;
        }

        return invitedTeam;
    }

    private void kick(CommandSender commandSender, String[] strings) {
        Player player = (Player) commandSender;
        try {
            Team team = plugin.getTeamManager().getTeamFromPlayer((Player) commandSender);

            if (team == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
                return;
            }

            if (Objects.equals(strings[1], player.getName())) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu ne peux pas te kick toi-même."));
                return;
            }

            Member member = team.getMembers().stream().filter(m -> Objects.equals(plugin.getServer().getOfflinePlayer(m.getPlayerUUID()).getName(), strings[1])).findFirst().orElse(null);

            if (member == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NOT_IN_TEAM));
                return;
            }
            team.removeMember(member);

            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(member.getPlayerUUID());
            player.spigot().sendMessage(TextComponentUtil.createSuccessMessage(offlinePlayer.getName() + " kické(e) !"));
            if (offlinePlayer.isOnline()) {
                Objects.requireNonNull(offlinePlayer.getPlayer()).spigot().sendMessage(TextComponentUtil.createInfoMessage("Vous avez été kické(e) de l'équipe."));
            }
        } catch (Exception e) {
            player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Erreur lors du kick du joueur."));
        }
    }

    private void promote(CommandSender commandSender, String[] strings) {
        Player player = (Player) commandSender;
        try {
            Team team = plugin.getTeamManager().getTeamFromPlayer((Player) commandSender);

            if (team == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
                return;
            }

            if (Objects.equals(strings[1], player.getName())) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu ne peux pas te promouvoir toi-même."));
                return;
            }

            Member member = team.getMembers().stream().filter(m -> Objects.equals(plugin.getServer().getOfflinePlayer(m.getPlayerUUID()).getName(), strings[1])).findFirst().orElse(null);

            if (member == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NOT_IN_TEAM));
                return;
            }

            if (Objects.equals(member.getRole(), MEMBER)) {
                team.updateMember(member, MODERATOR);
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(member.getPlayerUUID());
                player.spigot().sendMessage(TextComponentUtil.createSuccessMessage(offlinePlayer.getName() + " promu(e) !"));
                if (offlinePlayer.isOnline()) {
                    Objects.requireNonNull(offlinePlayer.getPlayer()).spigot().sendMessage(TextComponentUtil.createSuccessMessage("Vous avez été promu(e) !"));
                }
            } else if (Objects.equals(member.getRole(), MODERATOR)) {
                EnumMap<ClickEvent.Action, String> clickEvent = new EnumMap<>(ClickEvent.Action.class);
                clickEvent.put(ClickEvent.Action.SUGGEST_COMMAND, "/team transfer " + strings[1]);
                player.spigot().sendMessage(TextComponentUtil.createSpecialMessage("Ce joueur est déjà modérateur, si vous voulez le promouvoir chef à votre place, utilisez /team transfer <joueur>", List.of(clickEvent)));
            }
        } catch (Exception e) {
            LienubsTeam.getInstance().getLogger().severe("Erreur lors de la promotion du joueur: " + e.getMessage());
            player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Erreur lors de la promotion du joueur."));
        }

    }

    private void demote(CommandSender commandSender, String[] strings) {
        Player player = (Player) commandSender;
        try {
            Team team = plugin.getTeamManager().getTeamFromPlayer((Player) commandSender);

            if (team == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
                return;
            }

            if (Objects.equals(strings[1], player.getName())) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu ne peux pas te dégrader toi-même."));
                return;
            }

            Member member = team.getMembers().stream().filter(m -> Objects.equals(plugin.getServer().getOfflinePlayer(m.getPlayerUUID()).getName(), strings[1])).findFirst().orElse(null);

            if (member == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NOT_IN_TEAM));
                return;
            }

            if (Objects.equals(member.getRole(), MODERATOR)) {
                team.updateMember(member, MEMBER);
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(member.getPlayerUUID());
                player.spigot().sendMessage(TextComponentUtil.createSuccessMessage(offlinePlayer.getName() + " rétrogradé(e) !"));
                if (offlinePlayer.isOnline()) {
                    Objects.requireNonNull(offlinePlayer.getPlayer()).spigot().sendMessage(TextComponentUtil.createSuccessMessage("Vous avez été rétrogradé(e)."));
                }
            } else if (Objects.equals(member.getRole(), LEADER)) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Vous ne pouvez pas rétrograder le chef de l'équipe."));
            }
        } catch (Exception e) {
            player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Erreur lors de la dégradation du joueur."));
        }
    }

    private void disband(CommandSender commandSender) {
        try {
            // Disband the team the player is in
            Team team = plugin.getTeamManager().getTeamFromPlayer((Player) commandSender);
            if (team == null) {
                commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
                return;
            }

            List<Member> members = new ArrayList<>(team.getMembers());
            plugin.getTeamManager().disbandTeam(team);
            for (Member member : members) {
                Player player = plugin.getServer().getPlayer(member.getPlayerUUID());
                if (player != null) {
                    player.spigot().sendMessage(TextComponentUtil.createInfoMessage("L'équipe a été dissoute."));
                }
            }
            commandSender.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Équipe dissoute !"));

        } catch (Exception e) {
            commandSender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Erreur lors de la dissolution de l'équipe."));
            LienubsTeam.getInstance().getLogger().severe("Erreur lors de la dissolution de l'équipe: " + e.getMessage());
        }
    }

    private void tpa(CommandSender commandSender, String @NotNull [] strings) {
        // Teleport to a player
        Player player = (Player) commandSender;
        Player target = plugin.getServer().getPlayer(strings[1]);
        Team team = plugin.getTeamManager().getTeamFromPlayer(player);
        if (team == null) {
            player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
            return;
        }

        if (Objects.equals(strings[1], ACCEPT)) {
            acceptTpa(player);
        } else if (Objects.equals(strings[1], "deny")) {
            denyTpa(player);
        } else {
            if (target == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NOT_CONNECTED));
                return;
            }

            if (plugin.getTeamManager().getTeamFromPlayer(target) == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Ce joueur n'est pas dans une équipe."));
                return;
            }

            if (team != plugin.getTeamManager().getTeamFromPlayer(target)) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Ce joueur n'est pas dans ton équipe."));
            }

            plugin.getTeamManager().requestTeleport(player, target);
            target.spigot().sendMessage(TextComponentUtil.createInfoMessage(player.getName() + " veut se téléporter à toi. Acceptes-tu ?"));
            target.spigot().sendMessage(TextComponentUtil.createAcceptDenyMessage("/team tpa accept", "/team tpa deny"));
            player.spigot().sendMessage(TextComponentUtil.createInfoMessage("Demande de téléportation envoyée."));
        }
    }

    private void acceptTpa(@NotNull Player player) {
        player.spigot().sendMessage(TextComponentUtil.createInfoMessage(plugin.getTeamManager().getTpaRequests().toString()));
        if (!plugin.getTeamManager().getTpaRequests().containsKey(player.getUniqueId())) {
            player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu n'as pas de demande de téléportation."));
            return;
        }

        Player foundSender = plugin.getServer().getPlayer(plugin.getTeamManager().getTpaRequests().get(player.getUniqueId()));

        if (foundSender == null) {
            player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NOT_CONNECTED));
            return;
        }

        teleportSequence(foundSender, player);
    }

    private void denyTpa(@NotNull Player player) {
        if (!plugin.getTeamManager().getTpaRequests().containsKey(player.getUniqueId())) {
            player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu n'as pas de demande de téléportation."));
            return;
        }

        plugin.getTeamManager().getTpaRequests().remove(player.getUniqueId());
        player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Demande de téléportation refusée."));
    }

    private void teleportSequence(Player sender, Player target) {
        // Wait 3 seconds, if player moves, cancel teleportation
        // If player doesn't move, teleport player to target

        int count = 0;
        while (count < 3) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LienubsTeam.getInstance().getLogger().severe("Erreur lors de la téléportation: " + e.getMessage());
                sender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Erreur lors de la téléportation, contacte un administrateur."));

                Thread.currentThread().interrupt();
            }
            if (sender.getLocation().distanceSquared(sender.getLocation()) > 1) {
                sender.spigot().sendMessage(TextComponentUtil.createErrorMessage("Téléportation annulée, tu t'es déplacé."));
                plugin.getTeamManager().getTpaRequests().remove(target.getUniqueId());
                return;
            } else {
                sender.spigot().sendMessage(TextComponentUtil.createInfoMessage("Téléportation dans " + (3 - count) + " secondes..."));
            }
            count++;
        }

        sender.teleport(target.getLocation());
        target.spigot().sendMessage(TextComponentUtil.createSuccessMessage("Téléportation réussie !"));
        plugin.getTeamManager().getTpaRequests().remove(target.getUniqueId());
    }

    private void transfer(CommandSender commandSender, String[] strings) {
        Player player = (Player) commandSender;
        try {
            Team team = plugin.getTeamManager().getTeamFromPlayer((Player) commandSender);

            if (team == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NO_TEAM));
                return;
            }

            if (Objects.equals(strings[1], player.getName())) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Tu ne peux pas te transférer toi-même."));
                return;
            }

            Member member = team.getMembers().stream().filter(m -> Objects.equals(plugin.getServer().getOfflinePlayer(m.getPlayerUUID()).getName(), strings[1])).findFirst().orElse(null);
            Member senderMember = team.getMembers().stream().filter(m -> Objects.equals(plugin.getServer().getOfflinePlayer(m.getPlayerUUID()).getName(), player.getName())).findFirst().orElse(null);

            if (member == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage(NOT_IN_TEAM));
                return;
            }
            if (senderMember == null) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Erreur lors du transfert du leadership."));
                return;
            }

            if (Objects.equals(member.getRole(), MODERATOR) || Objects.equals(member.getRole(), MEMBER)) {
                team.updateMember(member, LEADER);
                team.updateMember(senderMember, MODERATOR);
                team.setLeader(member.getPlayerUUID());
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(member.getPlayerUUID());
                player.spigot().sendMessage(TextComponentUtil.createSuccessMessage(offlinePlayer.getName() + " promu(e) chef de l'équipe !"));
                if (offlinePlayer.isOnline()) {
                    Objects.requireNonNull(offlinePlayer.getPlayer()).spigot().sendMessage(TextComponentUtil.createSuccessMessage("Vous avez été promu(e) chef de l'équipe !"));
                }
            } else if (Objects.equals(member.getRole(), LEADER)) {
                player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Ce joueur est déjà chef de l'équipe."));
            }
        } catch (Exception e) {
            player.spigot().sendMessage(TextComponentUtil.createErrorMessage("Erreur lors du transfert du leadership."));
        }
    }
}
