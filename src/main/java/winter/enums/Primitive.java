package winter.enums;

import java.util.Set;
import java.util.function.Function;

public enum Primitive {
    String(Set.of("java.lang.String"), java.lang.String::valueOf),
    Integer(Set.of("java.lang.Integer", "int"), e -> java.lang.Integer.parseInt(java.lang.String.valueOf(e))),
    Double(Set.of("java.lang.Double", "double"), e -> java.lang.Double.parseDouble(java.lang.String.valueOf(e))),
    Boolean(Set.of("java.lang.Boolean", "boolean"), e -> java.lang.Boolean.parseBoolean(java.lang.String.valueOf(e))),
    Long(Set.of("java.lang.Long", "long"), e -> java.lang.Long.parseLong(java.lang.String.valueOf(e))),
    Float(Set.of("java.lang.Float", "float"), e -> java.lang.Float.parseFloat(java.lang.String.valueOf(e))),
    Short(Set.of("java.lang.Short", "short"), e -> java.lang.Short.parseShort(java.lang.String.valueOf(e))),
    Byte(Set.of("java.lang.Byte", "byte"), e -> java.lang.Byte.parseByte(java.lang.String.valueOf(e))),
    ;

    private final Set<String> names;
    private final Function<Object, Object> parser;

    Primitive(Set<java.lang.String> names, Function<Object, Object> parser) {
        this.names = names;
        this.parser = parser;
    }

    public Set<String> getNames() {
        return names;
    }

    public Function<Object, Object> getParser() {
        return parser;
    }
}
