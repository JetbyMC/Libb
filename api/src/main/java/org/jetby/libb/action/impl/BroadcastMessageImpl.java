package org.jetby.libb.action.impl;


import org.jetby.libb.action.Action;
import org.jetby.libb.action.ActionContext;
import org.jetby.libb.action.ActionInput;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class BroadcastMessageImpl implements Action {

    @Override
    public void execute(@NotNull ActionContext ctx, @NotNull ActionInput input) {

        Audience.audience(Bukkit.getOnlinePlayers()).sendMessage(input.getOrSerialize());

    }
}
