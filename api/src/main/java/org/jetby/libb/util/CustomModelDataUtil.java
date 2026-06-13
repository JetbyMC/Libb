package org.jetby.libb.util;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class CustomModelDataUtil {

    private static final Class<?> COMPONENT_CLASS;
    private static final Method GET_COMPONENT;
    private static final Method SET_COMPONENT;
    private static final Method SET_STRINGS;
    private static final Method GET_STRINGS;
    private static final Method SET_FLOATS;
    private static final Method GET_FLOATS;
    private static final Method SET_FLAGS;
    private static final Method GET_FLAGS;
    private static final boolean NEW_API;

    static {
        Class<?> cc = null;
        Method get = null, set = null;
        Method setStr = null, getStr = null;
        Method setFl = null, getFl = null;
        Method setFlg = null, getFlg = null;
        boolean supported = false;

        try {
            cc = Class.forName("org.bukkit.inventory.meta.components.CustomModelDataComponent");
            get    = ItemMeta.class.getMethod("getCustomModelDataComponent");
            set    = ItemMeta.class.getMethod("setCustomModelDataComponent", cc);
            setStr = cc.getMethod("setStrings", List.class);
            getStr = cc.getMethod("getStrings");
            setFl  = cc.getMethod("setFloats", List.class);
            getFl  = cc.getMethod("getFloats");
            setFlg = cc.getMethod("setFlags", List.class);
            getFlg = cc.getMethod("getFlags");

            get.setAccessible(true);
            set.setAccessible(true);
            setStr.setAccessible(true);
            getStr.setAccessible(true);
            setFl.setAccessible(true);
            getFl.setAccessible(true);
            setFlg.setAccessible(true);
            getFlg.setAccessible(true);

            supported = true;
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {}

        COMPONENT_CLASS = cc;
        GET_COMPONENT   = get;
        SET_COMPONENT   = set;
        SET_STRINGS     = setStr;
        GET_STRINGS     = getStr;
        SET_FLOATS      = setFl;
        GET_FLOATS      = getFl;
        SET_FLAGS       = setFlg;
        GET_FLAGS       = getFlg;
        NEW_API         = supported;
    }

    public static void apply(ItemMeta meta, Object raw) {
        if (raw == null) return;

        if (raw instanceof Integer i) {
            meta.setCustomModelData(i);
            return;
        }

        try {
            Object component = GET_COMPONENT.invoke(meta);

            if (COMPONENT_CLASS.isInstance(raw)) {
                SET_COMPONENT.invoke(meta, raw);
                return;
            }

            if (raw instanceof String s) {
                SET_STRINGS.invoke(component, List.of(s));
            } else if (raw instanceof Map<?, ?> map) {
                if (map.containsKey("strings"))
                    SET_STRINGS.invoke(component, map.get("strings"));
                if (map.containsKey("floats"))
                    SET_FLOATS.invoke(component,
                            ((List<Number>) map.get("floats")).stream()
                                    .map(Number::floatValue).toList());
                if (map.containsKey("flags"))
                    SET_FLAGS.invoke(component, map.get("flags"));
            } else if (raw instanceof ConfigurationSection section) {
                if (section.contains("strings"))
                    SET_STRINGS.invoke(component, section.getStringList("strings"));
                if (section.contains("floats"))
                    SET_FLOATS.invoke(component,
                            section.getDoubleList("floats").stream()
                                    .map(Double::floatValue).toList());
                if (section.contains("flags"))
                    SET_FLAGS.invoke(component, section.getBooleanList("flags"));
            }

            SET_COMPONENT.invoke(meta, component);

        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static boolean matches(ItemMeta meta, Object model) {
        if (model == null) return true;
        if (meta == null) return false;

        if (model instanceof Integer i) {
            return meta.hasCustomModelData() && meta.getCustomModelData() == i;
        }

        try {
            Object component = GET_COMPONENT.invoke(meta);

            if (model instanceof String s) {
                return ((List<String>) GET_STRINGS.invoke(component)).contains(s);
            }
            if (model instanceof ConfigurationSection section) {
                if (section.contains("strings") &&
                        !((List<String>) GET_STRINGS.invoke(component)).containsAll(section.getStringList("strings")))
                    return false;
                if (section.contains("flags") &&
                        !((List<Boolean>) GET_FLAGS.invoke(component)).containsAll(section.getBooleanList("flags")))
                    return false;
                if (section.contains("floats")) {
                    List<Float> expected = section.getDoubleList("floats").stream()
                            .map(Double::floatValue).toList();
                    if (!((List<Float>) GET_FLOATS.invoke(component)).containsAll(expected))
                        return false;
                }
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    public static Object parse(ConfigurationSection section, String key) {
        Object raw = section.get(key);
        return switch (raw) {
            case Integer i -> i;
            case String s -> s;
            case ConfigurationSection cmd -> cmd;
            case null, default -> null;
        };
    }
}