protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean for small degrees of freedom (as per FDistributionImpl documentation)
    if (d < 5.0) { // assuming this is the threshold value based on context
        ret = inverseCumulativeProbability(p, 1);
    } else {
        ret = d / (d - 2.0);
    }
    return ret;
}