protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean and numerator degrees of freedom in the formula
    ret = (getNumeratorDegreesOfFreedom() + 2.0) / (d - 2.0);
    return ret;
}