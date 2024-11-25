public static float max(final float a, final float b) {
    // Check for NaN values for both parameters and handle them accordingly.
    if (Float.isNaN(a)) {
        return (Float.isNaN(b)) ? Float.NaN : b;
    }
    if (Float.isNaN(b)) {
        return a;
    }

    // Check for positive or negative infinity values for both parameters and handle them according to IEEE 754 standard.
    if (Float.isInfinite(a) && Float.isInfinite(b)) {
        if (a > b) {
            return a; // Return the positive infinity if both are infinite, following IEEE 754.
        } else {
            return b;
        }
    }

    // Handle normal comparison for non-NaN and non-infinite numbers.
    return (a <= b) ? b : a;
}