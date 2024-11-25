protected double getInitialDomain(double p) {
    double d = getDenominatorDegreesOfFreedom();
    // Use the appropriate method to calculate the inverse CDF for an F-distribution.
    return FDistributionImpl.inverseCumulativeProbability(1, d, p);
}