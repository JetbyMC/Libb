package org.jetby.libb.action.impl;

import org.jetby.libb.LibbApi;
import org.jetby.libb.action.Action;
import org.jetby.libb.action.ActionContext;
import org.jetby.libb.action.ActionInput;
import org.jetby.libb.gui.parser.ParsedGui;
import org.jetby.libb.gui.parser.ParserContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenImpl implements Action {

    @Override
    public void execute(@NotNull ActionContext ctx, @NotNull ActionInput input) {
        Player player = ctx.getPlayer();
        if (player == null) return;

        new ParsedGui(player, LibbApi.Settings.PARSED_GUIS.get(input.rawText()), ctx.getPlugin(), ParserContext.of(ctx.getSerializer()))
                .open(player);
    }
}
