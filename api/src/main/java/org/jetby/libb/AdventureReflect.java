package org.jetby.libb;

import net.kyori.adventure.text.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

public class AdventureReflect {

    private static Method miniMessageDeserialize;
    private static Object miniMessageInstance;

    private static Method gsonDeserializeString;
    private static Object gsonInstance;

    private static Method legacyAmpersandDeserialize;
    private static Object legacyAmpersandInstance;

    private static Method legacySectionDeserialize;
    private static Object legacySectionInstance;

    private static Method legacySerialize;

    private static Class<?> tagResolverClass;

    public static void init(URLClassLoader loader) {
        try {
            Class<?> mmClass = loader.loadClass("net.kyori.adventure.text.minimessage.MiniMessage");
            try {
                miniMessageInstance = mmClass.getMethod("miniMessage").invoke(null);
            } catch (NoSuchMethodException e) {
                miniMessageInstance = mmClass.getMethod("get").invoke(null);
            }
            for (Method m : mmClass.getMethods()) {
                if (m.getName().equals("deserialize")
                        && m.getParameterCount() == 2
                        && m.getParameterTypes()[0] == String.class
                        && m.getParameterTypes()[1].isArray()) {
                    miniMessageDeserialize = m;
                    break;
                }
            }
            if (miniMessageDeserialize == null) {
                throw new RuntimeException("MiniMessage.deserialize(String, TagResolver[]) not found");
            }
            tagResolverClass = loader.loadClass("net.kyori.adventure.text.minimessage.tag.resolver.TagResolver");
        } catch (Exception e) {
            throw new RuntimeException("Failed to init MiniMessage bridge", e);
        }

        try {
            Class<?> componentClass = loader.loadClass("net.kyori.adventure.text.Component");
            Class<?> gsonClass = loader.loadClass("net.kyori.adventure.text.serializer.gson.GsonComponentSerializer");
            gsonInstance = gsonClass.getMethod("gson").invoke(null);
            for (Method m : gsonClass.getMethods()) {
                if (m.getName().equals("deserialize") && m.getParameterCount() == 1) {
                    gsonDeserializeString = m;
                    break;
                }
            }
            if (gsonDeserializeString == null) {
                throw new RuntimeException("GsonComponentSerializer.deserialize not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to init Gson bridge", e);
        }

        try {
            Class<?> legacyClass = loader.loadClass("net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer");
            legacyAmpersandInstance = legacyClass.getMethod("legacyAmpersand").invoke(null);
            legacySectionInstance = legacyClass.getMethod("legacySection").invoke(null);
            legacyAmpersandDeserialize = legacyClass.getMethod("deserialize", String.class);
            legacySectionDeserialize = legacyAmpersandDeserialize;
            for (Method m : legacySectionInstance.getClass().getMethods()) {
                if (m.getName().equals("serialize") && m.getParameterCount() == 1) {
                    m.setAccessible(true);
                    legacySerialize = m;
                    break;
                }
            }
            if (legacySerialize == null) {
                throw new RuntimeException("LegacyComponentSerializer.serialize not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to init Legacy bridge", e);
        }
    }

    private static Component fromLegacy(Object adventureComponent) {
        try {
            String legacy = (String) legacySerialize.invoke(legacySectionInstance, adventureComponent);
            return Component.text(legacy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Component miniMessage(String input) {
        if (DependencyLoader.isNativeAdventure()) {
            return net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(input);
        }
        try {
            Object emptyArray = Array.newInstance(tagResolverClass, 0);
            Object component = miniMessageDeserialize.invoke(miniMessageInstance, input, emptyArray);
            return fromLegacy(component);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Component legacyAmpersand(String input) {
        if (DependencyLoader.isNativeAdventure()) {
            return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand().deserialize(input);
        }
        try {
            Object component = legacyAmpersandDeserialize.invoke(legacyAmpersandInstance, input);
            return fromLegacy(component);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Component legacySection(String input) {
        if (DependencyLoader.isNativeAdventure()) {
            return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(input);
        }
        try {
            Object component = legacySectionDeserialize.invoke(legacySectionInstance, input);
            return fromLegacy(component);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toLegacySection(Component component) {
        if (DependencyLoader.isNativeAdventure()) {
            return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(component);
        }
        if (component instanceof net.kyori.adventure.text.TextComponent tc) {
            return tc.content();
        }
        return "";
    }

    public static Component gson(String input) {
        if (DependencyLoader.isNativeAdventure()) {
            return net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().deserialize(input);
        }
        try {
            Object component = gsonDeserializeString.invoke(gsonInstance, input);
            return fromLegacy(component);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}