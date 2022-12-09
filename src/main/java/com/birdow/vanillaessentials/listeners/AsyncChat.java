package com.birdow.vanillaessentials.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.leoko.advancedban.MethodInterface;
import me.leoko.advancedban.Universal;
import me.leoko.advancedban.manager.PunishmentManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.*;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.*;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class AsyncChat implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncChatEvent event) {
        if (!configEnabled("chat")) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        List<Player> recipients = new ArrayList<>(Bukkit.getOnlinePlayers());
        String message = miniMessage().stripTags(LegacyComponentSerializer.legacyAmpersand().serialize(event.message())).replace("&", "").replace("§", "");
        String globalSymbol = Objects.requireNonNull(getConfigString("chat.settings.globalSymbol"));
        if (message.equals("") || message.equals(globalSymbol)) return;

        if (getConfigBoolean("Hooks.AdvancedBan")) {
            PunishmentManager api = PunishmentManager.get();
            MethodInterface mi = Universal.get().getMethods();
            if (api.getMute(mi.getInternUUID(player)) != null) return;
        }

        boolean isGlobal = false;

        if (message.startsWith(globalSymbol)) {
            if (!getConfigBoolean("chat.settings.global")) {
                player.sendMessage(deserialize(player, message("globalDisabled")));
                return;
            }
            isGlobal = true;
            message = message.replaceFirst(globalSymbol, "");
        } else {
            recipients.removeIf(recipient -> isFar(player, recipient));
        }

        for (Player recipient : recipients) {
            String style = message(isGlobal ? "globalPrefix" : "localPrefix") + message("chatFormat").replace("<message>", message);
            if (!getPlayerDataBoolean(recipient.getName() + ".isChatToggled")) {
                sendPlayerMessage(player, recipient, style);
            }
        }
        if (getConfigBoolean("chat.settings.notify") && recipients.size() == 1) {
            player.sendMessage(deserialize(player, message("chatNotify")));
        }
        log((isGlobal ? "G " : "L ") + player.getName() + ": " + message);
    }

    private boolean isFar(Player player1, Player player2) {
        if (player1.getWorld() != player2.getWorld()) return true;
        double distance = player1.getLocation().distance(player2.getLocation());
        return distance > getConfigInt("chat.settings.localRadius");
    }
}