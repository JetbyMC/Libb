package org.jetby.libb;

import org.jetby.libb.color.Serializer;
import org.jetby.libb.gui.parser.Gui;

import java.util.HashMap;
import java.util.Map;

public interface LibbApi {

    public static class Settings {
        public static Serializer CONFIG_COLORIZER;
        public static final Map<String, Gui> PARSED_GUIS;

        static {
            CONFIG_COLORIZER = Serializer.UNIFIED;
            PARSED_GUIS = new HashMap();
        }
    }
}
