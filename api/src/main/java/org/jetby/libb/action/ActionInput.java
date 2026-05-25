package org.jetby.libb.action;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetby.libb.LibbApi;
import org.jetby.libb.color.Serializer;

public record ActionInput(@NotNull String rawText,
                          @Nullable Component serialized
) {

    @NotNull
    public Component getOrSerialize(@NotNull Serializer serializer) {
        if (serialized == null) {
            return serializer.deserialize(rawText);
        }
        return serialized;
    }

    @NotNull
    public Component getOrSerialize() {
        return getOrSerialize(LibbApi.Settings.CONFIG_COLORIZER);
    }

}
