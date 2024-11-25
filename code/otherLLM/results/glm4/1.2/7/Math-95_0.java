protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();

    // Handle edge case where degrees of freedom is 2 or less to avoid division by zero
    if (d <= 2.0) {
        throw new IllegalArgumentException("Degrees of freedom must be greater than 2");
    }

    // Use the mean as the initial domain estimate
    ret = d / (d - 2.0);

    return ret;
}