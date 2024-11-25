public enum NumberType {
    LONG("l", "L"),
    FLOAT("f", "F"),
    DOUBLE("d", "D");

    private final String[] indicators;

    NumberType(String... indicators) {
        this.indicators = indicators;
    }

    public static NumberType getType(char c) {
        for (NumberType type : values()) {
            if (type.indicators[0].equals(String.valueOf(c))) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid number type indicator: " + c);
    }

    public Object createNumber(String str) throws NumberFormatException {
        switch (this) {
            case LONG:
                return createLong(str);
            case FLOAT:
                return createFloat(str);
            case DOUBLE:
                return createDouble(str);
            default:
                throw new UnsupportedOperationException("Unsupported number type: " + this);
        }
    }

    private Object createLong(String str) throws NumberFormatException {
        // implementation
    }

    private Object createFloat(String str) throws NumberFormatException {
        // implementation
    }

    private Object createDouble(String str) throws NumberFormatException {
        // implementation
    }
}