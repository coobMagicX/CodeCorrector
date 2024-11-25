public static float max(final float a, final float b) {
    // Check for NaN values first
    if (Float.isNaN(a)) return b;
    if (Float.isNaN(b)) return a;

    // Check for infinity and negative infinity
    if ((a > 0 || Float.isInfinite(a)) && (b < 0 || Float.isInfinite(b))) return a;
    if ((a < 0 || Float.isInfinite(a)) && (b > 0 || Float.isInfinite(b))) return b;

    // If neither value is NaN or infinity, use the standard comparison
    return (a <= b) ? b : a;
}