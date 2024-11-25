protected double getInitialDomain(double p) {
    double d = getDenominatorDegreesOfFreedom();

    if (d <= 2.0) {
        // For small degrees of freedom, use the mean directly.
        return d;
    } else {
        // For larger degrees of freedom, calculate using the mean formula
        return d / (d - 2.0);
    }
}