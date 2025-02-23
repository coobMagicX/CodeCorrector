public double cumulativeProbability(double x) throws MathException {
    if (x == Double.NEGATIVE_INFINITY) {
        return 0.0;
    } else if (x == Double.POSITIVE_INFINITY) {
        return 1.0;
    } else {
        double dev = (x - mean) / (standard