public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == o2;
    } else if (isArray(o1)) {
        return isArray(o2) && areArraysEqual(o1, o2);
    } else {
        // Check for instanceof before calling equals to handle cases where
        // the objects are of different types but represent the same value.
        if (!(o1 instanceof o2.getClass())) {
            return false;
        }
        return o1.equals(o2);
    }
}

// Implement the missing methods below if not already provided:
static boolean isArray(Object o) {
    return o.getClass().isArray();
}

static boolean areArrayLengthsEqual(Object o1, Object o2) {
    return ((Object[]) o1).length == ((Object[]) o2).length;
}

static boolean areArrayElementsEqual(Object o1, Object o2) {
    if (o1 instanceof Object[] && o2 instanceof Object[]) {
        for (int i = 0; i < ((Object[]) o1).length; i++) {
            if (!areEqual(((Object[]) o1)[i], ((Object[]) o2)[i])) {
                return false;
            }
        }
    } else {
        throw new IllegalArgumentException("Both arguments must be arrays.");
    }
}