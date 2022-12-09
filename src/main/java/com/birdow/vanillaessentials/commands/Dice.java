package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigInt;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class Dice extends CustomCommand {

    public Dice() { super("dice"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            try {
                if (Integer.parseInt(args[0]) > 0 && Integer.parseInt(args[0]) <= getConfigInt("dice.maxDices")) {
                    Random random = new Random();
                    List<String> results = Arrays.asList(message("dice1"), message("dice2"), message("dice3"), message("dice4"), message("dice5"), message("dice6"));
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < Integer.parseInt(args[0]); i++) {
                        sb.append(results.get(random.nextInt(results.size()))).append(" ");
                    }
                    String message = message("diceFormat").replace("<result>", sb.toString());
                    for (Player recipient : player.getLocation().getNearbyPlayers(getConfigInt("chat.settings.rpRadius"))) {
                        sendPlayerMessage(player, recipient, message);
                    }
                } else throw new Exception();
            } catch (Exception e) {
                sendHelpMessage(player, message("invalidDices").replace("<maxDices>", String.valueOf(getConfigInt("dice.maxDices"))), "/dice ");
            }
        } else {
            sendHelpMessage(player, message("diceCommandUsage"), "/dice ");
        }
    }

    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("1", "2", "3");
        return Lists.newArrayList();
    }
}