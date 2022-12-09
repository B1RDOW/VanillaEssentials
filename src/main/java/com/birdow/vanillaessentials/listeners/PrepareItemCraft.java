package com.birdow.vanillaessentials.listeners;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import static com.birdow.vanillaessentials.utils.ConfigManager.configEnabled;
import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigBoolean;
import static com.birdow.vanillaessentials.utils.Messages.deserialize;
import static com.birdow.vanillaessentials.utils.Messages.message;

public class PrepareItemCraft implements Listener {

    @EventHandler
    public void prepareCraftEvent(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();
        if (result != null) {
            if (result.getType() == Material.FILLED_MAP && result.getAmount() >= 2 && configEnabled("mapsClone")) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                if (getConfigBoolean("mapsClone.notify")) {
                    for (HumanEntity humanEntity : event.getViewers()) {
                        Player player = (Player) humanEntity;
                        player.sendMessage(deserialize(player, message("mapsCloning")));
                    }
                }
            }
            if (result.getType() == Material.WRITTEN_BOOK && result.getAmount() >= 1 && configEnabled("booksClone")) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                if (getConfigBoolean("booksClone.notify")) {
                    for (HumanEntity humanEntity : event.getViewers()) {
                        Player player = (Player) humanEntity;
                        player.sendMessage(deserialize(player, message("booksCloning")));
                    }
                }
            }
        }
    }
}
