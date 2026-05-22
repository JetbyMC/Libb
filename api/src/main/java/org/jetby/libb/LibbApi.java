package org.jetby.libb;

import org.jetby.libb.color.Serializer;
import org.jetby.libb.gui.parser.Gui;
import org.jetby.libb.platform.Platform;
import org.jetby.libb.platform.PlatformDetector;

import java.util.HashMap;
import java.util.Map;

public interface LibbApi {

    Platform PLATFORM = Settings.PLATFORM;

    static void init() {
        Settings.PLATFORM = PlatformDetector.detect();
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
