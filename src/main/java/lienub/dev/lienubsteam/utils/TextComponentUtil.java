package lienub.dev.lienubsteam.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextComponentUtil {
    public static TextComponent createMessage(String message, ChatColor color) {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setColor(color);
        return textComponent;
    }

    public static TextComponent createErrorMessage(String message) {
        TextComponent errorComponent = new TextComponent("✖ ");
        errorComponent.setColor(ChatColor.RED);
        TextComponent messageComponent = new TextComponent("| " +message);
        messageComponent.setColor(ChatColor.WHITE);
        errorComponent.addExtra(messageComponent);

        return errorComponent;

    }

    public static TextComponent createInfoMessage(String message) {
        TextComponent infoComponent = new TextComponent("ℹ ");
        infoComponent.setColor(ChatColor.AQUA);
        TextComponent messageComponent = new TextComponent("| " +message);
        messageComponent.setColor(ChatColor.WHITE);
        infoComponent.addExtra(messageComponent);

        return infoComponent;
    }

    public static TextComponent createSuccessMessage(String message) {
        TextComponent successComponent = new TextComponent("✔ ");
        successComponent.setColor(ChatColor.GREEN);
        TextComponent messageComponent = new TextComponent("| " +message);
        messageComponent.setColor(ChatColor.WHITE);
        successComponent.addExtra(messageComponent);

        return successComponent;
    }

    public static TextComponent createHoverMessage(String message, String hoverText) {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(hoverText)}));
        return textComponent;
    }

    public static TextComponent createClickMessage(String message, String clickValue) {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, clickValue));
        return textComponent;
    }
}
