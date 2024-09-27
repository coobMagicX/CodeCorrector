public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) && Arrays.equals((Object[]) o1, (Object[]) o2);
    } else {
        return o1.equals(o2);
    }
}

static boolean isArray(Object o) {
    return o.getClass().isArray();
}