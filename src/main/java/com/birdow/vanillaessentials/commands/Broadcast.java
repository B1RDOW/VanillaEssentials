package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.birdow.vanillaessentials.utils.Messages.*;

public class Broadcast extends CustomCommand {

    public Broadcast() { super("broadcast", true, "broadcast"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            String message = message("broadcast").replace("<message>", extractMessage(args));
            for (Player recipient : Bukkit.getOnlinePlayers()) {
                recipient.playSound(recipient.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1, 1);
                sendPlayerMessage(sender, recipient, message);
            }
        } else {
            sendHelpMessage(sender, message("bcCommandUsage"), "/bc ");
        }
    }
}
