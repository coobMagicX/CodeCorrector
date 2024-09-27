public double cumulativeProbability(double x) throws MathException {
    double scaledX = (x - mean) / (standardDeviation * Math.sqrt(2.0));

    // Checking for extreme values to prevent overflow in the error function
    if (scaledX < -10) {
        return 0.0;
    } else if (scaledX > 10) {
        return 1.0;
    } else {
        return 0.5 * (1.0 + Erf.erf(scaledX));
    }
}