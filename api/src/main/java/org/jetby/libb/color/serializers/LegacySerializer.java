package org.jetby.libb.color.serializers;

import net.kyori.adventure.text.Component;
import org.jetby.libb.AdventureReflect;
import org.jetby.libb.color.Serializer;

public class LegacySerializer implements Serializer {
    private final char character;

    public LegacySerializer(char character) {
        this.character = character;
    }

    @Override
    public Component deserialize(String input) {
        return character == '&'
                ? AdventureReflect.legacyAmpersand(input)
                : AdventureReflect.legacySection(input);
    }
}