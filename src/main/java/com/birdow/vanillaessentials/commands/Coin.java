package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigInt;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class Coin extends CustomCommand {

    public Coin() { super("coin"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            Random random = new Random();
            String result = random.nextBoolean() ? message("eagle") : message("tail");
            String message = message("coinFormat").replace("<result>", result);
            for (Player recipient : player.getLocation().getNearbyPlayers(getConfigInt("chat.settings.rpRadius"))) {
                sendPlayerMessage(player, recipient, message);
            }
        } else {
            sendHelpMessage(player, message("coinCommandUsage"), "/coin");
        }
    }
}
