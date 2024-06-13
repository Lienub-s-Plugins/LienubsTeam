package lienub.dev.lienubsteam.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.Map;

public class TextComponentUtil {
    public static TextComponent createMessage(String message, ChatColor color) {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setColor(color);
        return textComponent;
    }

    public static TextComponent createErrorMessage(String message) {
        TextComponent errorComponent = new TextComponent("✖  ");
        errorComponent.setColor(ChatColor.RED);
        TextComponent messageComponent = new TextComponent("| " +message);
        messageComponent.setColor(ChatColor.GRAY);
        errorComponent.addExtra(messageComponent);

        return errorComponent;

    }

    public static TextComponent createInfoMessage(String message) {
        TextComponent infoComponent = new TextComponent("ℹ ");
        infoComponent.setColor(ChatColor.AQUA);
        TextComponent messageComponent = new TextComponent("| " +message);
        messageComponent.setColor(ChatColor.GRAY);
        infoComponent.addExtra(messageComponent);

        return infoComponent;
    }

    public static TextComponent createSuccessMessage(String message) {
        TextComponent successComponent = new TextComponent("✔  ");
        successComponent.setColor(ChatColor.GREEN);
        TextComponent messageComponent = new TextComponent("| " +message);
        messageComponent.setColor(ChatColor.GRAY);
        successComponent.addExtra(messageComponent);

        return successComponent;
    }

    public static TextComponent createSpecialMessage(String message, List<Map<ClickEvent.Action, String>> actions) {
        TextComponent textComponent = new TextComponent(message);

        for (Map<ClickEvent.Action, String> action : actions) {
            for (Map.Entry<ClickEvent.Action, String> entry : action.entrySet()) {
                textComponent.setClickEvent(new ClickEvent(entry.getKey(), entry.getValue()));
            }
        }
        return textComponent;
    }

    public static TextComponent createAcceptDenyMessage(String acceptCommand, String denyCommand) {
        TextComponent acceptComponent = new TextComponent("✔  ");
        acceptComponent.setColor(ChatColor.GREEN);
        acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCommand));
        acceptComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Accepter")}));

        TextComponent denyComponent = new TextComponent("✖  ");
        denyComponent.setColor(ChatColor.RED);
        denyComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, denyCommand));
        denyComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Refuser")}));

        TextComponent messageComponent = new TextComponent("| ");
        messageComponent.setColor(ChatColor.WHITE);

        acceptComponent.addExtra(messageComponent);
        acceptComponent.addExtra(denyComponent);

        return acceptComponent;
    }
}
