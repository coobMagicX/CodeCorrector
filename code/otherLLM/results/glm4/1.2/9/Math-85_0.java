public static double inverseCumulativeProbability(double p, double mean, double stdDev) {
    // Use z = invNorm(p) to find the z-score corresponding to the cumulative probability p
    return mean + (stdDev * NormalDistribution.inverseNormal(p));
}