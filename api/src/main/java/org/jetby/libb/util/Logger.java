package org.jetby.libb.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetby.libb.AdventureReflect;
import org.jetby.libb.LibbApi;
import org.jetby.libb.platform.Platform;

public class Logger {

    private final Plugin plugin;

    public Logger(Plugin plugin) {
        this.plugin = plugin;
    }

    public void info(String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().info(parse(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[INFO] [" + plugin.getName() + "] " + toLegacy(parse(message)));
        }
    }

    public void info(Component message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().info(message);
        } else {
            plugin.getLogger().info(toLegacy(message));
        }
    }

    public void warn(String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().warn(parse(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[WARN] [" + plugin.getName() + "] " + toLegacy(parse(message)));
        }
    }

    public void warn(String message, Object... objects) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().warn(parse(message), objects);
        } else {
            Bukkit.getConsoleSender().sendMessage("[WARN] [" + plugin.getName() + "] " + toLegacy(parse(message)));
        }
    }

    public void warn(Component message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().warn(message);
        } else {
            plugin.getLogger().warning(toLegacy(message));
        }
    }

    public void debug(String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().debug(parse(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[DEBUG] [" + plugin.getName() + "] " + toLegacy(parse(message)));
        }
    }

    public void debug(Component message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().debug(message);
        } else {
            plugin.getLogger().fine(toLegacy(message));
        }
    }

    public void error(String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().error(parse(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[ERROR] [" + plugin.getName() + "] " + toLegacy(parse(message)));
        }
    }

    public void error(Component message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().error(message);
        } else {
            plugin.getLogger().severe(toLegacy(message));
        }
    }

    public void error(String message, Object... objects) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().error(parse(message), objects);
        } else {
            Bukkit.getConsoleSender().sendMessage("[ERROR] [" + plugin.getName() + "] " + toLegacy(parse(message)));
        }
    }

    public void trace(String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().trace(parse(message));
        } else {
            plugin.getLogger().finest(toLegacy(parse(message)));
        }
    }

    public void trace(Component message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().trace(message);
        } else {
            plugin.getLogger().finest(toLegacy(message));
        }
    }


    // STATIC


    private static String toLegacy(Component component) {
        return AdventureReflect.toLegacySection(component);
    }

    private static Component parse(String message) {
        return AdventureReflect.miniMessage(message);
    }

    public static void info(Plugin plugin, String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().info(parse(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[INFO] [" + plugin.getName() + "] " + toLegacy(parse(message)));
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
            plugin.getComponentLogger().warn(parse(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[WARN] [" + plugin.getName() + "] " + toLegacy(parse(message)));
        }
    }

    public static void warn(Plugin plugin, String message, Object... objects) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().warn(parse(message), objects);
        } else {
            Bukkit.getConsoleSender().sendMessage("[WARN] [" + plugin.getName() + "] " + toLegacy(parse(message)));
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
            plugin.getComponentLogger().debug(parse(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[DEBUG] [" + plugin.getName() + "] " + toLegacy(parse(message)));
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
            plugin.getComponentLogger().error(parse(message));
        } else {
            Bukkit.getConsoleSender().sendMessage("[ERROR] [" + plugin.getName() + "] " + toLegacy(parse(message)));
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
            plugin.getComponentLogger().error(parse(message), objects);
        } else {
            Bukkit.getConsoleSender().sendMessage("[ERROR] [" + plugin.getName() + "] " + toLegacy(parse(message)));
        }
    }

    public static void trace(Plugin plugin, String message) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            plugin.getComponentLogger().trace(parse(message));
        } else {
            plugin.getLogger().finest(toLegacy(parse(message)));
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