public class BadEquals extends Object {
    private int value;

    public BadEquals(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BadEquals)) {
            return false;
        }
        return ((BadEquals) obj).value == this.value;
    }

    // Existing methods can be used as they are, but make sure to call super.equals() if necessary.
    public static boolean areEqual(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            return o1 == null && o2 == null;
        } else if (isArray(o1)) {
            // Ensure that the second object is also an array before proceeding.
            if (!isArray(o2)) {
                return false;
            }
            return areArraysEqual(o1, o2);
        } else if (o1 instanceof BadEquals && o2 instanceof BadEquals) {
            // When both objects are instances of BadEquals, use their overridden equals method.
            return ((BadEquals) o1).equals(o2);
        } else {
            // If neither object is an instance of BadEquals, or they're different types, use the default equals method.
            return o1.equals(o2);
        }
    }

    private static boolean isArray(Object o) {
        return o.getClass().isArray();
    }

    private static boolean areArrayLengthsEqual(Object o1, Object o2) {
        if (!isArray(o1) || !isArray(o2)) {
            return false;
        }
        return Array.getLength(o1) == Array.getLength(o2);
    }

    private static boolean areArrayElementsEqual(Object o1, Object o2) {
        if (!isArray(o1) || !isArray(o2)) {
            return false;
        }
        int length = Array.getLength(o1);
        for (int i = 0; i < length; i++) {
            if (!Array.get(o1, i).equals(Array.get(o2, i))) {
                return false;
            }
        }
        return true;
    }
}