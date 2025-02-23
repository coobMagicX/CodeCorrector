public double cumulativeProbability(double x) throws MathException {
    if (x == Double.NEGATIVE_INFINITY) {
        return 0.0;
    } else if (x == Double.POSITIVE_INFINITY) {
        return 1.0;
    }

    double dev = (x - mean) / (standardDeviation * Math.sqrt(2.0));

    // If the value of 'dev' becomes too extreme, erf function calculation can cause errors
    if (dev < -20