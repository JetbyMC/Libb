package org.jetby.libb.action.impl;

import org.jetby.libb.action.Action;
import org.jetby.libb.action.ActionContext;
import org.jetby.libb.action.ActionInput;
import org.jetby.libb.gui.parser.ParsedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RefreshImpl implements Action {

    @Override
    public void execute(@NotNull ActionContext ctx, @NotNull ActionInput input) {
        ParsedGui parsedGui = ctx.get(ParsedGui.class);

        Player player = ctx.getPlayer();
        if (player == null) return;

        if (parsedGui == null) return;
        parsedGui.refresh();
    }
}