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
        final double bi = b[i];

        final double ca = SPLIT_FACTOR * ai;
        final double cb = SPLIT_FACTOR * bi;

        // Directly calculate the high and low parts of scalar multiplication
        final double aHigh = ca - (ca - ai);
        final double aLow = ai - aHigh;
        final double bHigh = cb - (cb - bi);
        final double bLow = bi - bHigh;

        prodHigh[i] = ai * bi; // High part of the product
        final double prodLow = aLow * bLow - ((ai * bi) - (aHigh * bHigh) - (aLow * bHigh) - (aHigh * bLow));
        prodLowSum += prodLow;
    }

    final double prodHighCur = prodHigh[0];
    double prodHighNext = len > 1 ? prodHigh[1] : 0;
    double sHighPrev = prodHighCur + prodHighNext;
    double sPrime = sHighPrev - prodHighNext;

    // Initialize sum for low parts of high components
    double sLowSum = (prodHighNext - (sHighPrev - sPrime)) + (prodHighCur - sPrime);

    final int lenMinusOne = len - 1;
    for (int i = 1; i < lenMinusOne; i++) {
        prodHighNext = prodHigh[i + 1];
        final double sHighCur = sHighPrev + prodHighNext;
        sPrime = sHighCur - prodHighNext;

        // Add the low part of high components to the sum
        sLowSum += (prodHighNext - (sHighCur - sPrime)) + (sHighPrev - sPrime);
        sHighPrev = sHighCur;
    }

    double result = sHighPrev + prodLowSum; // Correctly combine the results

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