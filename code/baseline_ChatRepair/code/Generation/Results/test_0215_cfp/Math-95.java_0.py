protected double getInitialDomain(double p) {
    double d = getDenominatorDegreesOfFreedom();
    // Check if denominator degrees of freedom is less than or equal to 2
    if (d <= 2.0