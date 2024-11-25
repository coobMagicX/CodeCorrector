public static float max(final float a, final float b) {
    // If either number is NaN or both are infinite and have different signs, return NaN.
    if (Float.isNaN(a) || Float.isNaN(b)) {
        return Float.NaN;
    }
    // If both numbers are positive infinity or negative infinity and have the same sign, return one of them.
    if ((a == Float.POSITIVE_INFINITY && b == Float.POSITIVE_INFINITY) ||
        (a == Float.NEGATIVE_INFINITY && b == Float.NEGATIVE_INFINITY)) {
        return a;
    }
    // Otherwise, return the maximum value using comparison.
    return (a <= b) ? b : a;
}