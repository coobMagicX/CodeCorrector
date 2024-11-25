public double multiplied(double[] a, double[] b) {
    // Split numbers into high and low parts
    for (int i = 0; i < a.length; i++) {
        double ca = SPLIT_FACTOR * a[i];
        double aHigh = ca - (ca - a[i]);
        double aLow = a[i] - aHigh;
        
        double cb = SPLIT_FACTOR * b[i];
        double bHigh = cb - (cb - b[i]);
        double bLow = b[i] - bHigh;

        // Accurate multiplication
        double prodHigh = a[i] * b[i];
        double prodLow = aLow * bLow - (((prodHigh - aHigh * bHigh) - aLow * bHigh) - aHigh * bLow);

        // Update results
        prodHigh += resultHigh;
        prodLow += resultLow;
    }

    // Final rounding and check for NaN
    double result = prodHigh + prodLow;
    if (Double.isNaN(result)) {
        result = 0; // or any other value you consider appropriate
    }
    
    return result;
}