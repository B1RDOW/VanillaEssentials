package com.birdow.vanillaessentials.hooks;

import com.birdow.vanillaessentials.VanillaEssentials;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigBoolean;
import static com.birdow.vanillaessentials.utils.Messages.getPrefix;
import static com.birdow.vanillaessentials.utils.Messages.debug;
import static org.bukkit.Bukkit.getServer;

public class AdvancedBan {

    public static void setup() {
        if (!getConfigBoolean("Hooks.AdvancedBan")) {
            debug(Level.INFO, getPrefix() + " AdvancedBan hook disabled in config");
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("AdvancedBan") != null) {
            debug(Level.INFO, getPrefix() + " AdvancedBan successfully hooked");
        } else {
            debug(Level.WARNING, getPrefix() + " AdvancedBan didn't hook!");
            getServer().getPluginManager().disablePlugin(VanillaEssentials.getInstance());
        }
    }
}