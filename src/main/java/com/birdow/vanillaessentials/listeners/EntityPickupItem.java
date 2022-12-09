package com.birdow.vanillaessentials.listeners;

import com.birdow.vanillaessentials.hooks.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.birdow.vanillaessentials.commands.Ore.stringListToMaterialList;
import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.message;
import static com.birdow.vanillaessentials.utils.Messages.sendActionBarMessage;

public class EntityPickupItem implements Listener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {

            Item item = event.getItem();
            ItemStack itemStack = item.getItemStack();

            if (getPlayerDataBoolean(player.getName() + ".orePickupSell")) {
                List<Material> oreMaterials = new ArrayList<>(stringListToMaterialList(getConfigStringList("oreMoney.settings.sellItem")));
                if (!oreMaterials.contains(itemStack.getType())) return;
                if (!getConfigBoolean("oreMoney.settings.pickupInCreative")) {
                    if (player.getGameMode() == GameMode.CREATIVE) return;
                }
                Economy economy = Vault.getEconomy();
                event.setCancelled(true);
                item.remove();
                economy.depositPlayer(player, itemStack.getAmount() * getConfigInt("oreMoney.settings.oreMultiplier"));
                sendActionBarMessage(player, message("autoAdd").replace("<count>", String.valueOf(itemStack.getAmount())));
            }
        }
    }
}