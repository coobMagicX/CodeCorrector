public static float max(final float a, final float b) {
    return (a <= b) ? b : (Float.isNaN(a - b) ? Double.NaN : b);
}