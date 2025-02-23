public static double linearCombination(final double[] a, final double[] b) throws DimensionMismatchException {
    final int len = a.length;
    if (len != b.length) {
        throw new DimensionMismatchException(len, b.length);
    }

    
    if (len == 0) {
        return 0; 
    } else if (len == 1) {
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
        final double prodLow = aLow * bLow - (((prodHigh[i] - aHigh * bHigh) - aLow * bHigh) - aHigh * bLow);
        prodLowSum += prodLow;
    }

    
    double sum = prodHigh[0];
    double c = 0; 

    for (int i = 1; i < len; i++) {
        double y = prodHigh[i] - c; 
        double t = sum + y; 
        c = (t - sum) - y; 
        sum = t; 
    }

    sum = sum + (prodLowSum + c); 

    if (Double.isNaN(sum)) {
        
        sum = 0;
        for (int i = 0; i < len; i++) {
            sum += a[i] * b[i];
        }
    }

    return sum;
}
