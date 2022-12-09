package com.birdow.vanillaessentials.utils;

import com.birdow.vanillaessentials.VanillaEssentials;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.bukkit.Bukkit.getServer;

public class Messages {
    public static String colorTranslate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String placeholder(CommandSender sender, String message) {
        message = colorTranslate(message);
        if (sender instanceof Player) message = PlaceholderAPI.setPlaceholders((Player) sender, message);
        return message.replace("{", "<"). replace("}", ">");
    }

    public static Component deserialize(CommandSender sender, String message) {
        message = colorTranslate(message);
        if (sender instanceof Player) message = PlaceholderAPI.setPlaceholders((Player) sender, message);
        return miniMessage().deserialize(message.replace("{", "<"). replace("}", ">"));
    }

    public static String message(String message) {
        String configMessage = getLanguageConfigString("messages." + message);
        if (configMessage != null) return configMessage;
        else return colorTranslate("Сообщение messages." + message + " не найдено!");
    }

    public static void sendPlayerMessage(CommandSender player, Player recipient, String message) {
        if (getConfigBoolean("chat.settings.enableJson")) {
            String command = message("commandOnMessageClick").replace("<player>", player.getName());
            String json = placeholder(player, stringListToLines(getLanguageConfigStringList("messages.chatMessageJson")).replace("<player>", player.getName()));
            recipient.sendMessage(miniMessage().deserialize(placeholder(recipient, message), Placeholder.component("player",
                    miniMessage().deserialize("<hover:show_text:'" + json + "'>" + "<click:suggest_command:'" + command + "'>" + player.getName() + "</click></hover>"))));
        } else {
            recipient.sendMessage(miniMessage().deserialize(placeholder(recipient, message), Placeholder.component("player", text(player.getName()))));
        }
    }

    public static void sendHelpMessage(CommandSender player, String message, String command) {
        command = command.replace("<player>", player.getName());
        player.sendMessage(miniMessage().deserialize("<click:suggest_command:'" + command + "'>" + placeholder(player, message) + "</click>"));
    }

    public static void sendRunCommandMessage(CommandSender player, String message, String command) {
        command = command.replace("<player>", player.getName());
        player.sendMessage(miniMessage().deserialize("<click:run_command:'" + command + "'>" + placeholder(player, message) + "</click>"));
    }

    public static void sendActionBarMessage(CommandSender player, String message) {
        Audience.audience(player).sendActionBar(miniMessage().deserialize(placeholder(player, message)));
    }

    public static String stringListToLines(List<String> messages) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String message : messages) {
            i++;
            sb.append(message);
            if (i == messages.size()) break;
            sb.append("<newline>");
        }
        return sb.toString();
    }

    public static void debug(Level level, String message) {
        if (getConfigBoolean("Settings.debugMode")) {
            getServer().getLogger().log(level, message);
        }
    }

    public static void log(String message) {
        Logger.getLogger("Minecraft").info(message);
    }

    public static boolean noPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission("vanillaessentials." + permission)) {
            sender.sendMessage(deserialize(sender, message("noPermission")));
            log(sender.getName() + " issued server command without permission§c vanillaessentials." + permission);
            return true;
        } else return false;
    }

    public static String extractMessage(String... message) {
        return miniMessage().stripTags(LegacyComponentSerializer.legacyAmpersand().serialize(text(String.join(" ", message)))).replace("&", "").replace("§", "");
    }

    public static String getPrefix() {
        return "[" + VanillaEssentials.getInstance().getDescription().getPrefix() + "]";
    }
}
