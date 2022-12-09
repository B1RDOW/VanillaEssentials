package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigInt;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class Do extends CustomCommand {

    public Do() { super("do"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length > 0) {
            String message = message("doFormat").replace("<action>", extractMessage(args));
            for (Player recipient : player.getLocation().getNearbyPlayers(getConfigInt("chat.settings.rpRadius"))) {
                sendPlayerMessage(player, recipient, message);
            }
        } else {
            sendHelpMessage(player, message("doCommandUsage"), "/do ");
        }
    }
}
