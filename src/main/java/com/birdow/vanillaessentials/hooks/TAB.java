package com.birdow.vanillaessentials.hooks;

import com.birdow.vanillaessentials.VanillaEssentials;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigBoolean;
import static com.birdow.vanillaessentials.utils.Messages.getPrefix;
import static com.birdow.vanillaessentials.utils.Messages.debug;
import static org.bukkit.Bukkit.getServer;

public class TAB {

    public static void setup() {
        if (!getConfigBoolean("Hooks.TAB")) {
            debug(Level.INFO, getPrefix() + " TAB hook disabled in config");
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("TAB") == null) {
            debug(Level.WARNING, getPrefix() + " TAB didn't hook!");
            getServer().getPluginManager().disablePlugin(VanillaEssentials.getInstance());
        } else {
            debug(Level.INFO, getPrefix() + " TAB successfully hooked");
        }
    }
}
