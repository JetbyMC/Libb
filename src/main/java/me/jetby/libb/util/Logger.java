package me.jetby.libb.util;

import me.jetby.libb.Libb;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;

public class Logger {

    public static void info(Plugin plugin, String message) {
        plugin.getComponentLogger().info(Libb.MINI_MESSAGE.deserialize(message));
    }

    public static void info(Plugin plugin, Component message) {
        plugin.getComponentLogger().info(message);
    }

    public static void warn(Plugin plugin, String message) {
        plugin.getComponentLogger().warn(Libb.MINI_MESSAGE.deserialize(message));
    }

    public static void warn(Plugin plugin, String message, Object... objects) {
        plugin.getComponentLogger().warn(Libb.MINI_MESSAGE.deserialize(message), objects);
    }

    public static void warn(Plugin plugin, Component message) {
        plugin.getComponentLogger().warn(message);
    }

    public static void debug(Plugin plugin, String message) {
        plugin.getComponentLogger().debug(Libb.MINI_MESSAGE.deserialize(message));
    }

    public static void debug(Plugin plugin, Component message) {
        plugin.getComponentLogger().debug(message);
    }

    public static void error(Plugin plugin, String message) {
        plugin.getComponentLogger().error(Libb.MINI_MESSAGE.deserialize(message));
    }

    public static void error(Plugin plugin, Component message) {
        plugin.getComponentLogger().error(message);
    }

    public static void trace(Plugin plugin, String message) {
        plugin.getComponentLogger().trace(Libb.MINI_MESSAGE.deserialize(message));
    }

    public static void trace(Plugin plugin, Component message) {
        plugin.getComponentLogger().trace(message);
    }
}
