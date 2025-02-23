public double cumulativeProbability(double x) throws MathException {
    if (x - mean > 40 * standardDeviation) {
        return 1.0;
    } else if (x - mean < -40 * standardDeviation) {
        return 0.0;
    } else {
        try {
            return 0.5 * (1.0 + Erf.erf