protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mode (i.e., numerator degrees of freedom)
    ret = 1 / (1 - p * (d - 2.0) / d);
    return ret;
}