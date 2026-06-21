package org.jetby.libb.gui.parser;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetby.libb.gui.ParsableGui;

import java.util.function.Consumer;

public class ParsedGui extends ParsableGui {


    public ParsedGui(@NotNull Player viewer, @NotNull Gui guiDefinition, JavaPlugin plugin) {
        super(viewer, guiDefinition, plugin);
    }

    public ParsedGui(@NotNull Player viewer, @NotNull Gui guiDefinition, JavaPlugin plugin, ParserContext parserContext) {
        super(viewer, guiDefinition, plugin, parserContext);
    }

    public ParsedGui(@NotNull Player viewer, @NotNull FileConfiguration config, JavaPlugin plugin) {
        super(viewer, config, plugin);
    }

    public ParsedGui(@NotNull Player viewer, @NotNull FileConfiguration config, JavaPlugin plugin, ParserContext parserContext) {
        super(viewer, config, plugin, parserContext);
    }

    public void setReplace(String key, String input) {
        super.setReplace(key, input);
    }

    public void setReplace(Item item, String key, String input) {
        super.setReplace(item, key, input);
    }

    public ParsedGui addClickHandler(String sectionKey, Consumer<ConfigurableClickEvent> handler) {
        super.addClickHandler(sectionKey, handler);
        return this;
    }


    @Override
    public void setupLifecycleListeners() {
        refreshHelpfulPlaceholders();
        super.setupLifecycleListeners();
    }

    public void refreshHelpfulPlaceholders() {
        setReplace("{material}", item -> item.material().name());
    }
}