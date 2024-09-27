public static double linearCombination(final double[] a, final double[] b)
    throws DimensionMismatchException {
    final int len = a.length;
    if (len != b.length) {
        throw new DimensionMismatchException(len, b.length);
    }
    
    final double SPLIT_FACTOR = 134217729.0; // 2^27 + 1, used to split doubles for error-free multiplication

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

        prodHigh[i] = aHigh * bHigh;
        final double prodLow = aLow * bLow - (((prodHigh[i] -
                                                aHigh * bHigh) -
                                               aLow * bHigh) -
                                              aHigh * bLow);
        prodLowSum += prodLow;
    }

    double prodHighCur = prodHigh[0];
    double sHighPrev = prodHighCur;
    double sLowSum = 0;

    for (int i = 1; i < len; i++) {
        double prodHighNext = prodHigh[i];
        final double sHighCur = sHighPrev + prodHighNext;
        final double sPrime = sHighCur - prodHighNext;
        sLowSum += (prodHighNext - (sHighCur - sPrime)) + (sHighPrev - sPrime);
        sHighPrev = sHighCur;
    }

    double result = sHighPrev + (prodLowSum + sLowSum);

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