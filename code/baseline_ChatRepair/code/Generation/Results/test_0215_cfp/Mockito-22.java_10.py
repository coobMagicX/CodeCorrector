import java.util.Arrays;

public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1) && isArray(o2)) {
        // Properly checking array equality
        return Arrays.deepEquals(new Object[]{o1}, new Object[]{o2});
    } else {
        // Standard object equality
        return o1.equals(o2);
    }
}

private static boolean isArray(Object obj) {
    return obj != null && obj.getClass().isArray();
}
