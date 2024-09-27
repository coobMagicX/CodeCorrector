protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean
    if (d <= 2.0) {
        throw new IllegalArgumentException("degrees of freedom must be greater than 2.");
    }
    if (d <= p) {
        throw new IllegalArgumentException("degrees of freedom must be greater than p.");
    }
    ret = d / (d - p);
    return ret;
}