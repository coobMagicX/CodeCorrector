public double cumulativeProbability(double x) throws MathException {
    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    double cumulativeProb = 0.5 * (1.0 + Erf.erf(z));

    // Handle negative inputs correctly
    if (x < mean) {
        cumulativeProb = 1.0 - cumulativeProb;
    }

    // Adjust for extreme values
    if (cumulativeProb < 0.0) {
        cumulativeProb = 0.0;
    } else if (cumulativeProb > 1.0) {
        cumulativeProb = 1.0;
    }

    return cumulativeProb;
}