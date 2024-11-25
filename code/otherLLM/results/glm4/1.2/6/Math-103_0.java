public double cumulativeProbability(double x) throws MathException {
    if (standardDeviation <= 0.0) {
        throw new IllegalArgumentException(
            "Standard deviation must be positive.");
    }

    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    // Use the built-in erf function which is more accurate than a simple polynomial approximation.
    return 0.5 * (1.0 + Erf.erf(z));
}