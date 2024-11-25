public static float max(final float a, final float b) {
    if (Float.isNaN(a)) return Float.NaN; // Handle NaN correctly
    if (Float.isNaN(b)) return Float.NaN; // Handle NaN correctly
    
    if (Float.isInfinite(a)) {
        return (a == 0.0f || a < 0.0f ? -Float.MAX_VALUE : Float.MAX_VALUE); // Correctly compare infinity and NaN
    }
    
    if (Float.isInfinite(b)) {
        return (b == 0.0f || b < 0.0f ? -Float.MAX_VALUE : Float.MAX_VALUE); // Correctly compare infinity and NaN
    }
    
    if (a == b) return a; // Verify zero handling
    
    return (a <= b) ? b : a; // Ensure consistent behavior for reversed inputs
}