package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.birdow.vanillaessentials.utils.ConfigManager.setPlayerData;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class Pvp extends CustomCommand {

    public Pvp() { super("pvp"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                setPlayerData(player.getName() + ".pvp", true);
                player.sendMessage(deserialize(player, message("pvpEnabled")));
            }
            if (args[0].equalsIgnoreCase("off")) {
                setPlayerData(player.getName() + ".pvp", false);
                player.sendMessage(deserialize(player, message("pvpDisabled")));
            }
        } else {
            sendHelpMessage(player, message("pvpCommandUsage"), "/pvp ");
        }
    }
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("on", "off");
        return Lists.newArrayList();
    }
}