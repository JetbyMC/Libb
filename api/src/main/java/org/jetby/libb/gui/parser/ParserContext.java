package org.jetby.libb.gui.parser;

import org.jetby.libb.color.Serializer;

import java.util.HashMap;
import java.util.Map;

public record ParserContext(
        Serializer serializer,
        Map<Class<?>, Object> actionsObjects /* custom objects that will be added to ActionExecute */

) {

    public static ParserContext of(Serializer serializer) {
        return new ParserContext(serializer, null);
    }


    public static ParserContext of(Serializer serializer, Map<Class<?>, Object> actionsObjects) {
        return new ParserContext(serializer, actionsObjects);
    }


    public static ParserContext of(Serializer serializer, Object... actionsObjects) {
        if (actionsObjects == null) {
            return new ParserContext(serializer, null);
        }
        Map<Class<?>, Object> objectMap = new HashMap<>();
        for (Object object : actionsObjects) {
            if (object == null) continue;
            objectMap.put(object.getClass(), object);
        }
        return new ParserContext(serializer, objectMap);
    }
}
