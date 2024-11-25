public static float max(final float a, final float b) {
    if (Float.isInfinite(b)) return b;
    if (Float.isInfinite(a)) return a;
    if (Float.isNaN(a + b)) return Float.NaN;
    return (a <= b) ? b : a;
}