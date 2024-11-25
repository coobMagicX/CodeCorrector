public static double linearCombination(final double[] a, final double[] b)
throws DimensionMismatchException {
    final int len = a.length;
    if (len != b.length) {
        throw new DimensionMismatchException(len, b.length);
    }

    // Revisit array length check and correct implementation for single-element arrays
    if (len == 1) {
        return a[0] * b[0];
    }

    final double[] prodHigh = new double[len];
    double prodLowSum = 0;

    for (int i = 0; i < len; i++) {
        final double ai = a[i];
        final double ca = SPLIT_FACTOR * ai;
        final double aHigh = ca - (ca - ai);
        final double aLow = ai - aHigh;

        final double bi = b[i];
        final double cb = SPLIT_FACTOR * bi;
        final double bHigh = cb - (cb - bi);
        final double bLow = bi - bHigh;
        prodHigh[i] = ai * bi;
        final double prodLow = aLow * bLow - (((prodHigh[i] -
                                                aHigh * bHigh) -
                                               aLow * bHigh) -
                                              aHigh * bLow);
        prodLowSum += prodLow;
    }

    // Address potential NaN issues
    if (Double.isNaN(prodLowSum)) {
        return 0; // or any other suitable value for single-element arrays
    }

    final double[] scaledA = scale(SPLIT_FACTOR, a);
    final double[] scaledB = scale(SPLIT_FACTOR, b);

    final int lenMinusOne = len - 1;
    double result = 0;
    for (int i = 0; i < lenMinusOne; i++) {
        result += scaledA[i] * scaledB[i + 1];
    }
    result += scaledA[len - 1] * scaledB[len];

    return result;
}