public double cumulativeProbability(double x) throws MathException {
    if (x == Double.NEGATIVE_INFINITY) {
        return 0.0;
    } else if (x == Double.POSITIVE_INFINITY) {
        return 1.0;
    }

    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    
    try {
        // Calculation using the error function
        return