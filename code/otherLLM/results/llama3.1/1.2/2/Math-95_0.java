protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use degrees of freedom and numerator degrees of freedom for F distribution
    if (getNumeratorDegreesOfFreedom() == 1.0) {
        ret = 1 / (d - 2);
    } else {
        ret = d / (d - 2.0);
    }
    return ret;
}