protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean for denominator degrees of freedom equal to 2
    if (d == 2.0) {
        ret = getMean();
    } else {
        // use formula for other cases
        ret = d / (d - 2.0);
    }
    return ret;
}