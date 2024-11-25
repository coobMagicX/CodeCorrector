public double cumulativeProbability(double x) throws MathException {
    if (Double.isNaN(x) || Double.isInfinite(x)) {
        throw new IllegalArgumentException("Input value must not be NaN or Infinite.");
    }

    // Use the complementary error function to handle extreme values more safely
    double erfValue = Erf.erfc((x - getMean()) / (getStandardDeviation() * Math.sqrt(2.0)));
    
    // Avoiding overflow by clamping the result between 0 and 1
    if (erfValue < 0) {
        erfValue = 0;
    } else if (erfValue > 1) {
        erfValue = 1;
    }

    return 0.5 * (1.0 + erfValue);
}