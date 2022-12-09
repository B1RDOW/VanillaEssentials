package com.birdow.vanillaessentials.commands.links;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.birdow.vanillaessentials.commands.links.Links.sendLinksSetting;
import static com.birdow.vanillaessentials.utils.Messages.sendHelpMessage;
import static com.birdow.vanillaessentials.utils.Messages.message;

public class Faq extends CustomCommand {

    public Faq() { super("faq","links"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) sendLinksSetting(player, "faq");
        else sendHelpMessage(player, message("faqCommandUsage"), "/faq");
    }
}