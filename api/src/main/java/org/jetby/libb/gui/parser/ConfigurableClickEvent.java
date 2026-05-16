package org.jetby.libb.gui.parser;

import lombok.Getter;
import org.jetby.libb.gui.item.ItemWrapper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class ConfigurableClickEvent extends InventoryClickEvent {
    private final ConfigurationSection section;
    private final ItemWrapper wrapper;
    private final Item item;

    public ConfigurableClickEvent(InventoryClickEvent event, ConfigurationSection section, ItemWrapper wrapper, Item item) {
        super(event.getView(), event.getSlotType(), event.getSlot(), event.getClick(), event.getAction());
        this.section = section;
        this.wrapper = wrapper;
        this.item = item;
    }
}
