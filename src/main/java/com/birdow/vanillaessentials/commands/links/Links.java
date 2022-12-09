package com.birdow.vanillaessentials.commands.links;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.birdow.vanillaessentials.utils.ConfigManager.getConfig;
import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigString;
import static com.birdow.vanillaessentials.utils.Messages.*;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class Links extends CustomCommand {

    public Links() { super("links"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            ConfigurationSection section = getConfig().getConfigurationSection("links.settings");
            if (section == null) {
                player.sendMessage(deserialize(player, message("linksNotFound")));
                return;
            }
            try {
                for (String setting : section.getKeys(false)) {
                    sendLinksSetting(player, setting);
                    TimeUnit.MILLISECONDS.sleep(200);
                }
            } catch (InterruptedException e) {
                debug(Level.WARNING, getPrefix() + " Links execute error (Links.java)!\n" + e);
            }
        } else sendHelpMessage(player, message("linksCommandUsage"), "/links");
    }

    public static void sendLinksSetting(Player player, String setting) {
        String message = getConfigString("links.settings." + setting + ".message");
        String url = getConfigString("links.settings." + setting + ".url");
        String description = getConfigString("links.settings." + setting + ".description");
        player.sendMessage(miniMessage().deserialize("<hover:show_text:'" + placeholder(player, description) + "'>" + "<click:open_url:'" + url + "'>" + placeholder(player, message) + "</click></hover>"));
    }
}
