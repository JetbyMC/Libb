package org.jetby.libb.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetby.libb.LibbApi;
import org.jetby.libb.platform.Platform;

public class Logger {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static String toLegacy(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
    private static String toLegacy(String message) {
        return toLegacy(MINI_MESSAGE.deserialize(message));
    }
    public static void info(Plugin plugin, String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().info(MINI_MESSAGE.deserialize(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[INFO] ["+plugin.getName()+"] "+toLegacy(message));
        }
    }

    public static void info(Plugin plugin, Component message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().info(message);
        } else {
            plugin.getLogger().info(toLegacy(message));
        }
    }

    public static void warn(Plugin plugin, String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().warn(MINI_MESSAGE.deserialize(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[WARN] ["+plugin.getName()+"] "+toLegacy(message));
        }
    }

    public static void warn(Plugin plugin, String message, Object... objects) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().warn(MINI_MESSAGE.deserialize(message), objects);
        } else {
            Bukkit.getConsoleSender().sendMessage("[WARN] ["+plugin.getName()+"] "+toLegacy(message));
        }
    }

    public static void warn(Plugin plugin, Component message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().warn(message);
        } else {
            plugin.getLogger().warning(toLegacy(message));
        }
    }

    public static void debug(Plugin plugin, String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().debug(MINI_MESSAGE.deserialize(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[DEBUG] ["+plugin.getName()+"] "+toLegacy(message));
        }
    }

    public static void debug(Plugin plugin, Component message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().debug(message);
        } else {
            plugin.getLogger().fine(toLegacy(message));
        }
    }

    public static void error(Plugin plugin, String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().error(MINI_MESSAGE.deserialize(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[ERROR] ["+plugin.getName()+"] "+toLegacy(message));
        }
    }

    public static void error(Plugin plugin, Component message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().error(message);
        } else {
            plugin.getLogger().severe(toLegacy(message));
        }
    }

    public static void error(Plugin plugin, String message, Object... objects) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().error(MINI_MESSAGE.deserialize(message), objects);
        } else {
            Bukkit.getConsoleSender().sendMessage("[ERROR] ["+plugin.getName()+"] "+toLegacy(message));
        }
    }

    public static void trace(Plugin plugin, String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().trace(MINI_MESSAGE.deserialize(message));
        } else {
            plugin.getLogger().finest(toLegacy(message));
        }
    }

    public static void trace(Plugin plugin, Component message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().trace(message);
        } else {
            plugin.getLogger().finest(toLegacy(message));
        }
    }
}