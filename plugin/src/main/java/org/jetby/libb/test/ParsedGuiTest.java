package org.jetby.libb.test;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetby.libb.gui.parser.Gui;
import org.jetby.libb.gui.parser.ParsedGui;

public class ParsedGuiTest extends ParsedGui {

    public ParsedGuiTest(@NotNull Player viewer, @NotNull Gui guiDefinition, JavaPlugin plugin) {
        super(viewer, guiDefinition, plugin);

    }
}
