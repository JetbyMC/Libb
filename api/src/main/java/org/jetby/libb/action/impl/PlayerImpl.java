package org.jetby.libb.action.impl;


import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetby.libb.action.Action;
import org.jetby.libb.action.ActionContext;
import org.jetby.libb.action.ActionInput;

public class PlayerImpl implements Action {

    @Override
    public void execute(@NotNull ActionContext ctx, @NotNull ActionInput input) {
        Player player = ctx.getPlayer();
        if (player == null) return;

        player.chat(input.rawText());

    }
}
