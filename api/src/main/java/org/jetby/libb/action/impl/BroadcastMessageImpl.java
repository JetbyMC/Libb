package org.jetby.libb.action.impl;

import org.jetby.libb.action.Action;
import org.jetby.libb.action.ActionContext;
import org.jetby.libb.action.ActionInput;
import org.jetby.libb.platform.PlatformSender;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class BroadcastMessageImpl implements Action {

    @Override
    public void execute(@NotNull ActionContext ctx, @NotNull ActionInput input) {
        PlatformSender.sendMessage(Bukkit.getOnlinePlayers(), input.getOrSerialize(ctx.getSerializer()));
    }
}