public static float max(final float a, final float b) {
    // Validate inputs for NaN and infinity values
    if (Float.isInfinite(a)) return Math.abs(a) > Math.abs(b) ? a : b;
    if (Float.isInfinite(b)) return Math.abs(a) > Math.abs(b) ? a : b;

    // Correctly handle NaN and infinity values
    if (Float.isNaN(a + b)) return Float.NaN;

    // Return maximum value between two floating-point numbers
    return (a <= b) ? b : a;
}