public static float max(final float a, final float b) {
    return (a < b || (Float.isInfinite(a) && !Float.isInfinite(b))) ? b : (Float.isNaN(a + b) ? Float.NaN : a);
}