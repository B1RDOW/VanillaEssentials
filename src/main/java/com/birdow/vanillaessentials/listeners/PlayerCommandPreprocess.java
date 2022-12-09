package com.birdow.vanillaessentials.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpMap;

import java.util.List;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class PlayerCommandPreprocess implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();
        String[] args = new String[0];
        if (configEnabled("commandsTranslator")) {
            List<String> blockCommands = getConfigStringList("commandsTranslator.settings.disabledCommands");
            for (String blockCommand : blockCommands) {
                if (command.startsWith(blockCommand)) return;
            }
            //command = translateEnRu(command).toLowerCase();
            event.setMessage(command);
            args = command.split(" ");
            if (getConfigBoolean("commandsTranslator.settings.consoleNotify")) {
                log(message("consoleCommand").replace("<player>", player.getName()).replace("<command>", command));
            }
        }
        if (configEnabled("customUnknownCommandMessage")) {
            HelpMap map = Bukkit.getServer().getHelpMap();
            if (map.getHelpTopic(args[0]) == null) {
                event.setCancelled(true);
                sendRunCommandMessage(player, message("unknownCommand"), getConfigString("customUnknownCommandMessage.unknownCommand"));
            }
        }
        if (configEnabled("plHide")) {
            if (args[0].equalsIgnoreCase("/pl") || args[0].equalsIgnoreCase("/bukkit:pl") || args[0].equalsIgnoreCase("/plugins") || args[0].equalsIgnoreCase("/bukkit:plugins")) {
                if (!player.hasPermission("*") && !player.hasPermission("vanillaessentials.pl.bypass")) {
                    event.setCancelled(true);
                    player.sendMessage(deserialize(player, message("plHideMessage")));
                }
            }
        }
    }
}
