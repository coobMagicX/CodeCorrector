public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) && areArraysEqual(o1, o2);
    } else {
        return o1.equals(o2);
    }
}

static boolean isArray(Object o) {
    return o != null && o.getClass().isArray();
}

static boolean areArraysEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    }

    if (!o1.getClass().equals(o2.getClass())) {
        return false;
    }

    if (!o1.getClass().isArray()) {
        return o1.equals(o2);
    }

    int length1 = Array.getLength(o1);
    int length2 = Array.getLength(o2);

    if (length1 != length2) {
        return false;
    }

    for (int i = 0; i < length1; i++) {
        Object element1 = Array.get(o1, i);
        Object element2 = Array.get(o2, i);

        if (!areEqual(element1, element2)) {
            return false;
        }
    }

    return true;
}