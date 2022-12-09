package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class Ignore extends CustomCommand {

    public Ignore() { super("ignore", false, "chat.ignore"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (target == player) {
                    player.sendMessage(deserialize(player, message("ignoreYourself")));
                    return;
                }
                List<String> ignoredPlayers = getPlayerDataStringList(player.getName() + ".ignoredPlayers");
                if (ignoredPlayers.contains(target.getName())) {
                    ignoredPlayers.remove(target.getName());
                    setPlayerData(player.getName() + ".ignoredPlayers", ignoredPlayers);
                    sendPlayerMessage(target, player, message("unIgnored"));
                } else {
                    ignoredPlayers.add(target.getName());
                    setPlayerData(player.getName() + ".ignoredPlayers", ignoredPlayers);
                    sendPlayerMessage(target, player, message("ignoreNotify"));
                }
            } else {
                player.sendMessage(deserialize(player, message("playerNotOnline").replace("<player>", args[0])));
            }
        } else {
            sendHelpMessage(player, message("ignoreCommandUsage"), "/ignore ");
        }
    }

    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return null;
        return Lists.newArrayList();
    }
}
