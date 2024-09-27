public static double linearCombination(final double[] a, final double[] b)
    throws DimensionMismatchException {
    final int len = a.length;
    if (len != b.length) {
        throw new DimensionMismatchException(len, b.length);
    }

    // Revert to scalar multiplication.
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

    double result = 0;
    for (double v : prodHigh) {
        result += v;
    }
    result += prodLowSum;

    if (Double.isNaN(result)) {
        // either we have split infinite numbers or some coefficients were NaNs,
        // just rely on the naive implementation and let IEEE754 handle this
        result = 0;
        for (int i = 0; i < len; ++i) {
            result += a[i] * b[i];
        }
    }

    return result;
}