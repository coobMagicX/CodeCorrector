import org.apache.commons.math3.exception.DimensionMismatchException;

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
        final double ca = Math.ulp(ai) * ai;
        final double aHigh = ca - (ca - ai);
        final double aLow = ai - aHigh;

        final double bi = b[i];
        final double cb = Math.ulp(bi) * bi;
        final double bHigh = cb - (cb - bi);
        final double bLow = bi - bHigh;
        prodHigh[i] = ai * bi;
        final double prodLow = aLow * bLow - (((prodHigh[i] -
                                                aHigh * bHigh) -
                                               aLow * bHigh) -
                                              aHigh * bLow);
        prodLowSum += prodLow;
    }

    final double prodHighCur = prodHigh[0];
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

    double result = sHighPrev + (prodLowSum + sLowSum);

    if (Double.isNaN(result)) {
        result = 0;
        for (int i = 0; i < len; ++i) {
            result += a[i] * b[i];
        }
    }

    return result;
}