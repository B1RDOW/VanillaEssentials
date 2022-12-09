package com.birdow.vanillaessentials.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class VanillaExpansion extends PlaceholderExpansion {

    public @NotNull String getIdentifier() {
        return "vanilla";
    }

    public @NotNull String getAuthor() {
        return "BIRDOW";
    }

    public @NotNull String getVersion() {
        return "3.0.0";
    }

    public boolean canRegister() {
        return true;
    }

    public boolean persist() {
        return true;
    }

    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        if (params.equals("accent")) {
            return "{" + getColor("accent", player) + "}";
        }
        if (params.equals("primary")) {
            return "{" + getColor("primary", player) + "}";
        }
        if (params.equals("accent_")) {
            return getColor("accent", player);
        }
        if (params.equals("primary_")) {
            return getColor("primary", player);
        }
        if (params.equals("rep") || params.equals("reputation")) {
            return getFormattedReputation(player);
        }
        if (params.equals("pvp")) {
            return getFormattedPvpMode(player);
        }
        return "";
    }

    private String getFormattedReputation(Player player) {
        int reputation = getPlayerDataInt(player.getName() + ".reputation");
        String reputationString, positivePrefix = message("repPositivePrefix"), neutralPrefix = message("repNeutralPrefix"), negativePrefix = message("repNegativePrefix");
        if (reputation > 0) reputationString = positivePrefix + reputation;
        else if (reputation < 0) reputationString = negativePrefix + reputation;
        else reputationString = neutralPrefix + reputation;
        return placeholder(player, reputationString);
    }
    private String getFormattedPvpMode(Player player) {
        boolean pvpmode = getPlayerDataBoolean(player.getName() + ".pvp");
        return placeholder(player, pvpmode ? message("enabled") : message("disabled"));
    }

    private String getColor(String color, Player player) {
        String theme = getPlayerData().getString(player.getName() + ".theme");
        return colorTranslate(getConfigString("themes." + theme + "." + color + "Color"));
    }
}
