public static double linearCombination(final double[] a, final double[] b)
    throws DimensionMismatchException {
    final int len = a.length;
    if (len != b.length) {
        throw new DimensionMismatchException(len, b.length);
    }

    // Utilize the scale method where possible for vector multiplication.
    double[] scaledB = scale(a[0], b); // Scaling first element of a across all elements of b
    double result = 0;
    for (int i = 0; i < len; i++) {
        result += scaledB[i]; // Sum the scaled elements
    }

    return result;
}