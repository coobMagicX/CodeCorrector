protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean
    if (d <= 2.0) {
        // Handle cases where denominator degrees of freedom is less than or equal to 2
        throw new IllegalArgumentException("Denominator degrees of freedom must be greater than 2.");
    } else {
        ret = d / (d - 2.0);
    }
    return ret;
}