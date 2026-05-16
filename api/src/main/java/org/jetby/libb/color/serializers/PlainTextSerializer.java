package org.jetby.libb.color.serializers;

import org.jetby.libb.color.Serializer;
import net.kyori.adventure.text.Component;

public class PlainTextSerializer implements Serializer {

    @Override
    public Component deserialize(String input) {
        return Component.text(input);
    }
}