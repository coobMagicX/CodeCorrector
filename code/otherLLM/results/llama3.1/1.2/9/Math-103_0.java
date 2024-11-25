public double cumulativeProbability(double x) throws MathException {
    if (x < mean) { // Check for lower tail
        return 0.5 * (1.0 + Erf.erf((mean - x) /
                (standardDeviation * Math.sqrt(2.0))));
    } else { // Upper tail
        return 0.5 * (1.0 + Erf.erf((x - mean) /
                (standardDeviation * Math.sqrt(2.0))));
    }
}