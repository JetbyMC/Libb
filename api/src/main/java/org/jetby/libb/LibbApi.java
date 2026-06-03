package org.jetby.libb;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetby.libb.color.Serializer;
import org.jetby.libb.gui.parser.Gui;
import org.jetby.libb.platform.Platform;
import org.jetby.libb.platform.PlatformDetector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface LibbApi {

    Platform PLATFORM = Settings.PLATFORM;

    static void init() {
        Settings.PLATFORM = PlatformDetector.detect();
    }
    static List<Plugin> getDependentPlugins() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(plugin -> {
                    PluginDescriptionFile desc = plugin.getDescription();
                    return desc.getDepend().contains("Libb")
                            || desc.getSoftDepend().contains("Libb");
                })
                .collect(Collectors.toList());
    }

    class Settings {
        public static Serializer CONFIG_COLORIZER;
        public static final Map<String, Gui> PARSED_GUIS;
        public static Platform PLATFORM;

        static {
            CONFIG_COLORIZER = Serializer.UNIFIED;
            PARSED_GUIS = new HashMap();
        }
    }
}
