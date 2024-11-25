public static float max(final float a, final float b) {
    return (a <= b) ? b : ((Float.isInfinite(a) || Float.isInfinite(b)) ? 
        (Float.isInfinite(a) && !Float.isInfinite(b) ? a : b) : 
        (Float.isNaN(a + b) ? Float.NaN : b));
}