package winter.utils;

import winter.enums.Primitive;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Common
 *
 * @author Баярхүү.Лув, 2022.08.02
 */
public class Common {
    public static boolean isPrimitive(Object object) {
        return object instanceof String
                || object instanceof Integer
                || object instanceof Double
                || object instanceof Boolean
                || object instanceof Long
                || object instanceof Float
                || object instanceof Short
                || object instanceof Byte;
    }

    public static Object parse(Object object, Class<?> type) {
        for (Primitive primitive : Primitive.values()) {
            if (primitive.getNames().contains(type.getName())) {
                return primitive.getParser().apply(object);
            }
        }
        return object;
    }

    public static Map<String, Object> paramExtract(String params) {
        Map<String, Object> map = new HashMap<>();
        Arrays.stream(params.split("&")).forEach(param -> {
            String[] keyValue = param.split("=");
            map.put(keyValue[0], keyValue[1]);
        });
        return map;
    }
}
