public static float max(final float a, final float b) {
    return (a >= b) ? a : (Float.isNaN(a) || Float.isNaN(b) ? Float.NaN : b);
}