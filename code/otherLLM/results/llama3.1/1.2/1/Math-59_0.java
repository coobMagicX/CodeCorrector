public static float max(final float a, final float b) {
    return (a <= b && !Float.isInfinite(a)) ? 
           (b <= a && !Float.isInfinite(b) ? Float.NaN : b) :
           (a >= b && !Float.isInfinite(a) ? a : b);
}