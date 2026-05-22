package org.jetby.libb.platform;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetby.libb.LibbApi;

import java.util.List;
import java.util.stream.Collectors;

public class PlatformMeta {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    public static void setDisplayName(ItemMeta meta, Component component) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            meta.displayName(component);
        } else {
            meta.setDisplayName(LEGACY.serialize(component));
        }
    }

    public static Component getDisplayName(ItemMeta meta) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            return meta.displayName();
        } else {
            String name = meta.getDisplayName();
            return name == null || name.isEmpty() ? null : LEGACY.deserialize(name);
        }
    }

    public static void setLore(ItemMeta meta, List<Component> lore) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            meta.lore(lore);
        } else {
            meta.setLore(lore.stream().map(LEGACY::serialize).collect(Collectors.toList()));
        }
    }

    public static List<Component> getLore(ItemMeta meta) {
        if (LibbApi.Settings.PLATFORM == Platform.PAPER) {
            return meta.lore();
        } else {
            List<String> lore = meta.getLore();
            if (lore == null) return null;
            return lore.stream().map(LEGACY::deserialize).collect(Collectors.toList());
        }
    }
}