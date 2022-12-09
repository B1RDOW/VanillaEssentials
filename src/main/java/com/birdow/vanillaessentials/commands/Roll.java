package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigInt;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class Roll extends CustomCommand {

    public Roll() { super("roll"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            try {
                if (Integer.parseInt(args[0]) > 0 && Integer.parseInt(args[0]) <= getConfigInt("roll.maxRoll")) {
                    Random random = new Random();
                    String result = random.nextInt(Integer.parseInt(args[0]) + 1) + " из " + Integer.parseInt(args[0]);
                    String message = message("rollFormat").replace("<result>", result);
                    for (Player recipient : player.getLocation().getNearbyPlayers(getConfigInt("chat.settings.rpRadius"))) {
                        sendPlayerMessage(player, recipient, message);
                    }
                } else throw new Exception();
            } catch (Exception e) {
                sendHelpMessage(player, message("invalidRoll").replace("<maxRoll>",String.valueOf(getConfigInt("roll.maxRoll"))), "/roll ");
            }
        } else {
            sendHelpMessage(player, message("rollCommandUsage"), "/roll ");
        }
    }
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100");
        return Lists.newArrayList();
    }
}
