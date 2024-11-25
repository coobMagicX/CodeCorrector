public static float max(final float a, final float b) {
    return (a <= b) ? b : ((Float.isInfinite(b) && !Float.isInfinite(a)) || (!Float.isInfinite(b) && Float.isInfinite(a)) ? 
            (Float.isInfinite(a) ? a : b) : ((Float.isNaN(a + b) || signum(a + b) == 0.0) ? Float.NaN : b));
}