public static float max(final float a, final float b) {
    if (Float.isNaN(a)) {
        return b;
    }
    if (Float.isNaN(b)) {
        return a;
    }
    if (a == Float.POSITIVE_INFINITY || b == Float.POSITIVE_INFINITY) {
        return Float.POSITIVE_INFINITY;
    }
    if (a == Float.NEGATIVE_INFINITY || b == Float.NEGATIVE_INFINITY) {
        return Float.NEGATIVE_INFINITY;
    }
    return (a <= b) ? b : a;
}