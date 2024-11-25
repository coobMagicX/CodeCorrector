public static float max(final float a, final float b) {
    if (Float.isNaN(a) || Float.isNaN(b)) {
        // If either number is NaN, return NaN
        return Float.NaN;
    }
    if (Float.isInfinite(a) && Float.isInfinite(b)) {
        // If both numbers are infinite, we need to check their signs
        return (a > b) ? a : b;
    }
    return (a <= b) ? b : a;
}