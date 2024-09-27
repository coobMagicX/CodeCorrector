protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean
    if (d - 2.0 != 0 && d > 2.0) {
        ret = d / (d - 2.0);
    } else {
        throw new IllegalArgumentException("Invalid denominator degrees of freedom.");
    }
    return ret;
}