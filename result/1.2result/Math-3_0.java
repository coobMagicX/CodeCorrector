public static double linearCombination(final double[] a, final double[] b)
    throws DimensionMismatchException {
    if (a == null || b == null || a.length == 0 || b.length == 0 || a.length != b.length) {
        throw new DimensionMismatchException(a.length, b.length);
    }

    final int len = a.length;
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

    double result;
    if (len == 2) {
        result = prodHigh[0] + prodHigh[1] + prodLowSum;
    } else {
        double prodHighCur = prodHigh[0];
        double prodHighNext = prodHigh[1];
        double sHighPrev = prodHighCur + prodHighNext;
        double sPrime = sHighPrev - prodHighNext;
        double sLowSum = (prodHighNext - (sHighPrev - sPrime)) + (prodHighCur - sPrime);

        final int lenMinusOne = len - 1;
        for (int i = 1; i < lenMinusOne; i++) {
            prodHighNext = prodHigh[i + 1];
            final double sHighCur = sHighPrev + prodHighNext;
            sPrime = sHighCur - prodHighNext;
            sLowSum += (prodHighNext - (sHighCur - sPrime)) + (sHighPrev - sPrime);
            sHighPrev = sHighCur;
        }

        result = sHighPrev + (prodLowSum + sLowSum);
    }

    if (Double.isNaN(result)) {
        result = 0;
        for (int i = 0; i < len; ++i) {
            result += a[i] * b[i];
        }
    }

    return result;
}