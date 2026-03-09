package me.jetby.libb.gui.parser;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.jetby.libb.Libb;
import me.jetby.libb.action.ActionContext;
import me.jetby.libb.action.ActionExecute;
import me.jetby.libb.action.record.ActionBlock;
import me.jetby.libb.gui.AdvancedGui;
import me.jetby.libb.gui.item.ItemWrapper;
import me.jetby.libb.gui.parser.view.RequirementEvaluator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class ParsedGui {

    @Getter
    private final AdvancedGui holder;
    private final Player viewer;
    private final Gui guiSettings;

    private final Map<String, Consumer<ConfigurableClickEvent>> clickHandlers = new HashMap<>();

    private record ItemEntry(Item item, ItemWrapper wrapper) {}

    public ParsedGui(@NotNull Player viewer, @NotNull Gui guiDefinition) {
        this.guiSettings = guiDefinition;
        this.viewer       = viewer;

        this.holder = new AdvancedGui(
                Libb.MINI_MESSAGE.deserialize(
                        PlaceholderAPI.setPlaceholders(viewer, guiDefinition.title())),
                guiDefinition.size()
        );

        setupLifecycleListeners();
        buildItems(guiDefinition.items());
    }

    public ParsedGui(@NotNull Player viewer, @NotNull FileConfiguration config) {
        this.viewer       = viewer;

        this.holder = new AdvancedGui(
                config.getString("title", ""),
                config.getInt("size", 54)
        );
        this.guiSettings = new Gui(
                config.getString("id"),
                config.getString("title"),
                config.getInt("size"),
                config.getStringList("command"),
                config.getStringList("pre_open"),
                ParseUtil.getActionBlock(config, "on_open"),
                ParseUtil.getActionBlock(config, "on_close"),
                ParseUtil.getItems(config)
        );
        setupLifecycleListeners();
        buildItems(guiSettings.items());
    }

    private void setupLifecycleListeners() {
        holder.onOpen(event -> {
            if (guiSettings.onOpen() != null)
                ActionExecute.run(ActionContext.of(viewer).with(this), guiSettings.onOpen());
        });
        holder.onClose(event -> {
            if (guiSettings.onClose() != null)
                ActionExecute.run(ActionContext.of(viewer).with(this), guiSettings.onClose());
        });
    }

    public void refresh() {
        clearInventory();
        buildItems(guiSettings.items());
    }

    private void clearInventory() {
        holder.getItems().clear();
        holder.getInventory().clear();
    }

    private void buildItems(List<Item> items) {
        if (items == null) return;

        Map<Integer, List<Item>> slotCandidates = new LinkedHashMap<>();

        for (Item item : items) {
            if (item.itemStack() == null) continue;
            for (int slot : item.slots()) {
                slotCandidates.computeIfAbsent(slot, k -> new ArrayList<>()).add(item);
            }
        }

        Map<Integer, Item> slotWinners = new LinkedHashMap<>();

        for (Map.Entry<Integer, List<Item>> entry : slotCandidates.entrySet()) {
            int slot = entry.getKey();
            List<Item> sorted = new ArrayList<>(entry.getValue());
            sorted.sort(Comparator.comparingInt(Item::priority));
            for (Item candidate : sorted) {
                if (RequirementEvaluator.meetsAll(viewer, candidate.viewRequirements())) {
                    slotWinners.put(slot, candidate);
                    break;
                }
            }
        }

        Map<Item, List<Integer>> itemWonSlots = new LinkedHashMap<>();
        for (Map.Entry<Integer, Item> e : slotWinners.entrySet()) {
            itemWonSlots.computeIfAbsent(e.getValue(), k -> new ArrayList<>()).add(e.getKey());
        }

        for (Map.Entry<Item, List<Integer>> e : itemWonSlots.entrySet()) {
            Item item = e.getKey();
            List<Integer> wonSlots = e.getValue();

            String      key     = UUID.randomUUID().toString();
            ItemWrapper wrapper = buildItemWrapper(item, wonSlots);
            holder.setItem(key, wrapper);
        }
    }

    private ItemWrapper buildItemWrapper(Item item, List<Integer> wonSlots) {
        ItemWrapper wrapper = new ItemWrapper(item.itemStack());
        wrapper.slots(wonSlots.toArray(new Integer[0]));

        if (item.displayName() != null) {
            wrapper.setDisplayName(PlaceholderAPI.setPlaceholders(viewer, item.displayName()));
            wrapper.setRawDisplayName(item.displayName());
        }

        wrapper.setRawLore(item.lore());
        wrapper.setLore(applyPlaceholders(item.lore()));

        if (item.flags() != null)
            wrapper.flags(item.flags().toArray(new ItemFlag[0]));

        wrapper.onClick(event -> {
            event.setCancelled(true);
            Player clicker = (Player) event.getWhoClicked();
            dispatchItemClick(clicker, wrapper, item, event);
        });

        return wrapper;
    }

    private ItemWrapper buildItemWrapper(Item item) {
        return buildItemWrapper(item, item.slots());
    }

    private void dispatchItemClick(Player clicker, ItemWrapper wrapper, Item item,
                                   org.bukkit.event.inventory.InventoryClickEvent event) {
        if (item.onClick().containsKey(null))
            ActionExecute.run(ActionContext.of(clicker).with(wrapper).with(this).with(holder),
                    item.onClick().get(null));

        for (Map.Entry<ClickType, ActionBlock> entry : item.onClick().entrySet()) {
            ClickType requiredClick = entry.getKey();
            if (!event.getClick().equals(requiredClick)) continue;
            ActionExecute.run(ActionContext.of(clicker)
                            .with(wrapper)
                            .with(this)
                            .with(holder),
                    entry.getValue());
        }

        for (Map.Entry<String, Consumer<ConfigurableClickEvent>> handlerEntry : clickHandlers.entrySet()) {
            if (item.section() != null && item.section().contains(handlerEntry.getKey()))
                handlerEntry.getValue().accept(new ConfigurableClickEvent(event, item.section(), wrapper));
        }
    }

    private List<String> applyPlaceholders(List<String> lines) {
        List<String> result = new ArrayList<>(lines.size());
        for (String line : lines)
            result.add(PlaceholderAPI.setPlaceholders(viewer, line));
        return result;
    }

    public ParsedGui addClickHandler(String sectionKey, Consumer<ConfigurableClickEvent> handler) {
        clickHandlers.put(sectionKey, handler);
        return this;
    }
}