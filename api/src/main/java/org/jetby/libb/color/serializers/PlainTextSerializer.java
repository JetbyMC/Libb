package org.jetby.libb.color.serializers;

import net.kyori.adventure.text.Component;
import org.jetby.libb.color.Serializer;

public class PlainTextSerializer implements Serializer {

    @Override
    public Component deserialize(String input) {
        return Component.text(input);
    }
}