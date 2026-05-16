package org.jetby.libb.color.serializers;

import org.jetby.libb.color.Serializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MiniMessageSerializer implements Serializer {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public Component deserialize(String input) {
        return miniMessage.deserialize(input);
    }

}
