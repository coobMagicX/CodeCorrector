protected double getInitialDomain(double p) {
    if (p <= 0.0 || p >= 1.0) {
        throw new IllegalArgumentException("Probability must be between 0 and 1");
    }
    
    double d = getDenominatorDegreesOfFreedom();
    // Assuming that the domain is calculated based on an inverse cumulative probability
    double ret;

    // Use the mean for the initial domain calculation
    if (d <= 2.0) {
        // If the degrees of freedom are less than or equal to 2, it's not defined in the normal distribution.
        throw new IllegalArgumentException("Degrees of freedom must be greater than 2");
    } else {
        ret = d / (d - 2.0);
    }

    return ret;
}