package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.*;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class Pm extends CustomCommand {

    public Pm() { super("pm"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("sound") || args[0].equalsIgnoreCase("notify")) {
                if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on")) {
                    setPlayerData(player.getName() + ".pmSoundNotify", true);
                    player.sendMessage(deserialize(player, message("pmSoundNotifyEnabled")));
                    return;
                } else if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off")) {
                    setPlayerData(player.getName() + ".pmSoundNotify", false);
                    player.sendMessage(deserialize(player, message("pmSoundNotifyDisabled")));
                    return;
                }
            }
            Player target = Bukkit.getPlayer(args[0]);
            String message = extractMessage(args).substring(args[0].length() + 1);
            if (target != null) {
                if (target == player) {
                    player.sendMessage(deserialize(player, message("pmYourself")));
                    return;
                }
                if (getPlayerDataStringList(target.getName() + ".ignoredPlayers").contains(player.getName())) {
                    sendPlayerMessage(target, player, message("ignored"));
                    return;
                }
                sendPlayerMessage(player, target, message("pmFormatReceive").replace("<message>", message));

                String command = message("commandOnMessageClick").replace("<player>", target.getName());
                String json = placeholder(target, stringListToLines(getLanguageConfigStringList("messages.chatMessageJson")).replace("<player>", target.getName()));
                player.sendMessage(miniMessage().deserialize(placeholder(player, message("pmFormatSend").replace("<message>", message)), Placeholder.component("recipient",
                        miniMessage().deserialize("<hover:show_text:'" + json + "'>" + "<click:suggest_command:'" + command + "'>" + target.getName() + "</click></hover>"))));

                if (getPlayerDataBoolean(target.getName() + ".pmSoundNotify")) {
                    target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
            } else {
                player.sendMessage(deserialize(player, message("playerNotOnline").replace("<player>", args[0])));
            }
        } else {
            sendHelpMessage(player, message("pmCommandUsage"), "/pm ");
        }
    }

    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return null;
        if (args.length == 2 && (args[0].equalsIgnoreCase("sound") || args[0].equalsIgnoreCase("notify"))) {
            return Lists.newArrayList("on", "off");
        }
        return Lists.newArrayList();
    }
}