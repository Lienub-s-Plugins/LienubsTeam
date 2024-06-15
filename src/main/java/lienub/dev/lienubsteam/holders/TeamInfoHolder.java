package lienub.dev.lienubsteam.holders;

import lienub.dev.lienubsteam.LienubsTeam;
import lienub.dev.lienubsteam.utils.InventoriesUtil;
import lienub.dev.lienubsteam.utils.team.Member;
import lienub.dev.lienubsteam.utils.team.Team;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Inventory holder for the team info GUI.
 * This class is used to store the player's team information and display it in the GUI.
 *
 * @see InventoryHolder
 * @see Player
 * @see Team
 * @see Member
 * @see ItemStack
 * @see ItemMeta
 * @see OfflinePlayer
 * @see ChatColor
 * @see InventoriesUtil
 * @see LienubsTeam
 * @see Material
 * @see Inventory
 * @see List
 * @see Map
 * @see ArrayList
 * @see Math
 */
public class TeamInfoHolder implements InventoryHolder {

    private final Player player;
    private final List<Map<OfflinePlayer, String>> membersList = new ArrayList<>();
    private int currentPage = 0;
    private static final int MEMBERS_PER_PAGE = 14;
    private final Inventory inventory;

    /**
     * Instantiates a new Team info holder.
     *
     * @param player the player
     */
    public TeamInfoHolder(Player player) {
        this.player = player;

        // Get members list
        Team team = LienubsTeam.getInstance().getTeamManager().getTeamFromPlayer(player);
        List<Member> members = team.getMembers();
        for (Member member : members) {
            LienubsTeam.getInstance().getLogger().info("Member: " + member.getPlayerUUID());
            // Prevent duplicate entries
            if (membersList.stream().anyMatch(map -> map.containsKey(LienubsTeam.getInstance().getServer().getOfflinePlayer(member.getPlayerUUID())))) {
                continue;
            }
            membersList.add(Map.of(LienubsTeam.getInstance().getServer().getOfflinePlayer(member.getPlayerUUID()), member.getRole()));
        }

        this.inventory = LienubsTeam.getInstance().getServer().createInventory(this, 9 * 6, LienubsTeam.getInstance().getTeamManager().getTeamFromPlayer(player).getName());
        setup();

    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    private void setup() {
        // Set the outline of the inventory with glass panes of gray/orange color.
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassPaneMeta = InventoriesUtil.initItemMeta(glassPane, " ", null);
        glassPane.setItemMeta(glassPaneMeta);

        ItemStack orangeGlassPane = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta orangeGlassPaneMeta = InventoriesUtil.initItemMeta(orangeGlassPane, " ", null);
        orangeGlassPane.setItemMeta(orangeGlassPaneMeta);

        InventoriesUtil.setupInventoryOutline(6, this.inventory, glassPane, orangeGlassPane);

        // Leader head
        OfflinePlayer leader = LienubsTeam.getInstance().getServer().getOfflinePlayer(LienubsTeam.getInstance().getTeamManager().getTeamFromPlayer(player).getLeader());
        ItemStack skull = InventoriesUtil.getPlayerHead(LienubsTeam.getInstance().getServer().getOfflinePlayer(LienubsTeam.getInstance().getTeamManager().getTeamFromPlayer(player).getLeader()));
        ItemMeta skullMeta = InventoriesUtil.initItemMeta(skull, "Leader de l'équipe", List.of(leader.getName() != null ? leader.getName() : " "));
        skull.setItemMeta(skullMeta);
        this.inventory.setItem(1 * 9 + 3, skull);

        // Manage team
        ItemStack manageTeam = new ItemStack(Material.BOOK);
        ItemMeta manageTeamMeta = InventoriesUtil.initItemMeta(manageTeam, "Gérer l'équipe", List.of("Cliquez pour gérer l'équipe", "AJOUT A LA PROCHAINE VERSION"));
        manageTeam.setItemMeta(manageTeamMeta);
        this.inventory.setItem(1 * 9 + 5, manageTeam);

        // Members
        setMemberList();
    }

    private void setMemberList() {
        int totalPages = getTotalPages();

        // Clear the previous items
        for (int i = 9 * 3 + 1; i < 9 * 3 + 1 + MEMBERS_PER_PAGE + 2; i++) {
            if (this.inventory.getItem(i) != null) {
                continue;
            }

            this.inventory.setItem(i, null);
        }

        // Get the members for the current page
        List<Map<OfflinePlayer, String>> members = getMembersForPage(currentPage);
        for (int i = 0; i < members.size(); i++) {
            int index = 9 * 3 + 1 + i;

            OfflinePlayer member = members.get(i).keySet().iterator().next();
            String role = members.get(i).get(member);
            ItemStack memberItem = InventoriesUtil.getPlayerHead(member);
            ItemMeta memberMeta = setPlayerHeadMeta(memberItem, member, role);
            memberItem.setItemMeta(memberMeta);
            this.inventory.setItem(index, memberItem);
        }

        // Setting page control

        if (totalPages != 1) {
            if (currentPage != 0) {
                ItemStack previousPage = new ItemStack(Material.ARROW);
                ItemMeta previousPageMeta = InventoriesUtil.initItemMeta(previousPage, "Page précédente", null);
                previousPage.setItemMeta(previousPageMeta);
                this.inventory.setItem(9 * 5 + 3, previousPage);
            }

            if (currentPage != totalPages - 1) {
                ItemStack nextPage = new ItemStack(Material.ARROW);
                ItemMeta nextPageMeta = InventoriesUtil.initItemMeta(nextPage, "Page suivante", null);
                nextPage.setItemMeta(nextPageMeta);
                this.inventory.setItem(9 * 5 + 5, nextPage);
            }
        }

        ItemStack page = new ItemStack(Material.PAPER);
        ItemMeta pageMeta = InventoriesUtil.initItemMeta(page, "Page " + (currentPage + 1) + "/" + totalPages, null);
        page.setItemMeta(pageMeta);
        this.inventory.setItem(9 * 5 + 4, page);
    }

    @Nullable
    private static ItemMeta setPlayerHeadMeta(ItemStack memberItem, OfflinePlayer member, @NotNull String role) {
        if (role.equals("leader")) {
            role = ChatColor.GOLD + ChatColor.BOLD.toString() + role.toUpperCase() + ChatColor.RESET;
        } else if (role.equals("moderator")) {
            role = ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + role.toUpperCase() + ChatColor.RESET;
        } else {
            role = ChatColor.GRAY + ChatColor.BOLD.toString() + role.toUpperCase() + ChatColor.RESET;
        }

        List<String> lore;
        if (member.isOnline()) {
            lore = List.of(role, ChatColor.GREEN + ChatColor.BOLD.toString() + "En ligne" + ChatColor.RESET, "Cliquez pour faire une demande de téléportation");
        } else {
            lore = List.of(role, ChatColor.RED + ChatColor.BOLD.toString() + "Hors ligne" + ChatColor.RESET);
        }

        return InventoriesUtil.initItemMeta(memberItem, member.getName(), lore);
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) membersList.size() / MEMBERS_PER_PAGE);
    }


    private @NotNull List<Map<OfflinePlayer, String>> getMembersForPage(int page) {
        int start = page * MEMBERS_PER_PAGE;
        int end = Math.min(start + MEMBERS_PER_PAGE, membersList.size());
        return membersList.subList(start, end);
    }

    /**
     * Next page.
     */
    public void nextPage() {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
            setMemberList();
        }
    }

    /**
     * Previous page.
     */
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            setMemberList();
        }
    }
}
