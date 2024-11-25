public class BadEquals {
    private int value;

    public BadEquals(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BadEquals)) return false;
        BadEquals that = (BadEquals) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

// And the existing areEqual method:
public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) && areArraysEqual(o1, o2);
    } else {
        return o1.equals(o2);
    }
}

// Utilize the provided methods:
static boolean isArray(Object o) {
    return o.getClass().isArray();
}

static boolean areArraysEqual(Object o1, Object o2) {
    return areArrayLengthsEqual(o1, o2)
            && areArrayElementsEqual(o1, o2);
}