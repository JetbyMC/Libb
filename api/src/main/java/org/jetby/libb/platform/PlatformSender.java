package org.jetby.libb.platform;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetby.libb.AdventureReflect;
import org.jetby.libb.LibbApi;

import java.util.Collection;

public class PlatformSender {

    private static String toLegacy(Component component) {
        return AdventureReflect.toLegacySection(component);
    }

    public static void sendMessage(CommandSender player, Component component) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            player.sendMessage(component);
        } else {
            player.sendMessage(toLegacy(component));
        }
    }

    public static void sendMessage(Collection<? extends CommandSender> players, Component component) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            Audience.audience(players).sendMessage(component);
        } else {
            String legacy = toLegacy(component);
            players.forEach(p -> p.sendMessage(legacy));
        }
    }

    public static void sendActionBar(Player player, Component component) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            player.sendActionBar(component);
        } else {
            player.spigot().sendMessage(
                    net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                    net.md_5.bungee.api.chat.TextComponent.fromLegacyText(toLegacy(component))
            );
        }
    }

    public static void sendActionBar(Collection<? extends Player> players, Component component) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            Audience.audience(players).sendActionBar(component);
        } else {
            String legacy = toLegacy(component);
            players.forEach(p -> p.spigot().sendMessage(
                    net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                    net.md_5.bungee.api.chat.TextComponent.fromLegacyText(legacy)
            ));
        }
    }

    public static void sendTitle(Player player, Title title) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            player.showTitle(title);
        } else {
            String titleStr = toLegacy(title.title());
            String subStr = toLegacy(title.subtitle());
            player.sendTitle(titleStr, subStr, 10, 70, 20);
        }
    }

    public static void sendTitle(Collection<? extends Player> players, Title title) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            Audience.audience(players).showTitle(title);
        } else {
            String titleStr = toLegacy(title.title());
            String subStr = toLegacy(title.subtitle());
            players.forEach(p -> p.sendTitle(titleStr, subStr, 10, 70, 20));
        }
    }
}