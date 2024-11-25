public static boolean equals(double x, double y) {
    if (Double.isNaN(x) && Double.isNaN(y)) return true; // Both NaNs are considered equal
    if (Double.isInfinite(x) != Double.isInfinite(y)) return false; // Infinities with different signs are unequal
    if (Double.isInfinite(x)) return true; // Infinities with the same sign are equal
    if (Double.isNaN(x) || Double.isNaN(y)) return false; // A non-NaN value is never equal to a NaN
    int scale = 10; // Choose an appropriate scale for floating-point precision handling
    float xFloat = round((float)x, scale, RoundingMode.HALF_UP);
    float yFloat = round((float)y, scale, RoundingMode.HALF_UP);
    return xFloat == yFloat;
}