package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class Chat extends CustomCommand {

    public Chat() { super("chat"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("theme") || args[0].equalsIgnoreCase("themes")) {
                if (args.length < 2) {
                    sendAvailableThemes(player);
                } else {
                    setTheme(player, args[1]);
                }
            }
            if (args[0].equalsIgnoreCase("global")) {
                if (args.length < 2) {
                    sendHelpMessage(player, message("chatCommandUsage"), "/chat global ");
                } else {
                    if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on")) {
                        setPlayerData(player.getName() + ".isGlobalChatToggled", false);
                        player.sendMessage(deserialize(player, message("globalChatMessagesEnable")));
                    }
                    if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off")) {
                        setPlayerData(player.getName() + ".isGlobalChatToggled", true);
                        player.sendMessage(deserialize(player, message("globalChatMessagesDisable")));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("help")) {
                if (args.length < 2) {
                    sendHelpMessage(player, message("chatCommandUsage"), "/chat help ");
                } else {
                    if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on")) {
                        setPlayerData(player.getName() + ".isAutoMessageToggled", false);
                        player.sendMessage(deserialize(player, message("autoMessagesEnable")));
                    }
                    if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off")) {
                        setPlayerData(player.getName() + ".isAutoMessageToggled", true);
                        player.sendMessage(deserialize(player, message("autoMessagesDisable")));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("death")) {
                if (args.length < 2) {
                    sendHelpMessage(player, message("chatCommandUsage"), "/chat death ");
                } else {
                    if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on")) {
                        setPlayerData(player.getName() + ".isDeathMessageToggled", false);
                        player.sendMessage(deserialize(player, message("deathMessagesEnable")));
                    }
                    if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off")) {
                        setPlayerData(player.getName() + ".isDeathMessageToggled", true);
                        player.sendMessage(deserialize(player, message("deathMessagesDisable")));
                    }
                }
            }
            if (args[0].equalsIgnoreCase("joins")) {
                if (args.length < 2) {
                    sendHelpMessage(player, message("chatCommandUsage"), "/chat joins ");
                } else {
                    if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on")) {
                        setPlayerData(player.getName() + ".isJoinMessageToggled", false);
                        player.sendMessage(deserialize(player, message("joinMessagesEnable")));
                    }
                    if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off")) {
                        setPlayerData(player.getName() + ".isJoinMessageToggled", true);
                        player.sendMessage(deserialize(player, message("joinMessagesDisable")));
                    }
                }
            }
        } else {
            sendHelpMessage(player, message("chatCommandUsage"), "/chat ");
        }
    }

    public static void sendAvailableThemes(Player player) {
        ConfigurationSection section = getConfig().getConfigurationSection("themes");
        List<String> themes = new ArrayList<>(Objects.requireNonNull(section).getKeys(false));
        try {
            for (String theme : themes) {
                if (player.hasPermission(Objects.requireNonNull(getConfigString("themes." + theme + ".permission")))) {
                    String message = message("chatFormat")
                            .replace("<player>", "<" + getConfigString("themes." + theme + "." + "accentColor") + ">" + player.getName())
                            .replace("<message>", "<" + getConfigString("themes." + theme + "." + "primaryColor") + ">" + message("themesMessage").replace("<theme>", theme));
                    sendRunCommandMessage(player, message, "/chat theme " + theme);
                    TimeUnit.MILLISECONDS.sleep(200);
                }
            }
            player.sendMessage(deserialize(player, message("moreThemes")));
        } catch (InterruptedException e) {
            debug(Level.WARNING, getPrefix() + " sendAvailableThemes error (Chat.java)!\n" + e);
        }
    }

    public static void setTheme(Player player, String themeName) {
        ConfigurationSection section = getConfig().getConfigurationSection("themes");
        List<String> themes = new ArrayList<>(Objects.requireNonNull(section).getKeys(false));
        for (String theme : themes) {
            if (themeName.equalsIgnoreCase(theme)) {
                if (player.hasPermission(Objects.requireNonNull(getConfigString("themes." + theme + ".permission")))) {
                    setPlayerData(player.getName() + ".theme", theme);
                    player.sendMessage(deserialize(player, message("themeChanged")));
                    return;
                } else {
                    player.sendMessage(deserialize(player, message("noPermission")));
                    return;
                }
            }
        }
        player.sendMessage(deserialize(player, message("themeNotFound")));
    }

    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("themes", "global", "help", "death", "joins");
        if (args.length == 2 && !args[0].equalsIgnoreCase("theme") && !args[0].equalsIgnoreCase("themes")) {
            return Lists.newArrayList("on", "off");
        }
        return Lists.newArrayList();
    }
}
