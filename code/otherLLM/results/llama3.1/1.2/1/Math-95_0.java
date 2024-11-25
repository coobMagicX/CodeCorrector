protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean
    if (d == 2.0) { // handle edge case for df = 2
        return 1.0; // or any other appropriate value based on the distribution
    }
    ret = d / (d - 2.0);
    return ret;
}