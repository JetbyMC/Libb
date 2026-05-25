package org.jetby.libb.color.serializers;

import net.kyori.adventure.text.Component;
import org.jetby.libb.AdventureReflect;
import org.jetby.libb.color.Serializer;

public class MiniMessageSerializer implements Serializer {

    @Override
    public Component deserialize(String input) {
        return AdventureReflect.miniMessage(input);
    }

}
