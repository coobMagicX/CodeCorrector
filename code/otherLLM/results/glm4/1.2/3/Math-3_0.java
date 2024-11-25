public static double linearCombination(final double[] a, final double[] b)
throws DimensionMismatchException {
    final int len = a.length;
    if (len != b.length) {
        throw new DimensionMismatchException(len, b.length);
    }

    final double[] prodHigh = new double[len];
    double prodLowSum = 0;

    for (int i = 0; i < len; i++) {
        final double ai = a[i];
        final double bi = b[i];

        // Correct the calculation of high and low parts for multiplication.
        final double high = SPLIT_FACTOR * ai * bi;
        final double low = (1 - SPLIT_FACTOR) * ai * bi;

        prodHigh[i] = high;
        prodLowSum += low;  // Sum up the low parts separately
    }

    final double prodHighCur = prodHigh[0];
    double sHighPrev = prodHighCur + prodHigh[1]; // Assuming array is of length at least 2
    double sPrime = sHighPrev - prodHigh[1];

    double sLowSum = (prodHigh[1] - (sHighPrev - sPrime)) + (prodHighCur - sPrime);

    if (len > 2) {
        for (int i = 2; i < len; i++) {
            final double nextProdHigh = prodHigh[i];
            final double sHighCur = sHighPrev + nextProdHigh;
            sPrime = sHighCur - nextProdHigh;
            sLowSum += (nextProdHigh - (sHighCur - sPrime)) + (sHighPrev - sPrime);
            sHighPrev = sHighCur;
        }
    }

    double result = SPLIT_FACTOR * prodLowSum; // High part is not included as it's already handled separately

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