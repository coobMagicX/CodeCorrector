protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean
    if (d <= 2.0) {
        // Handle the case where denominator degrees of freedom is less than or equal to 2
        // Set ret to a default value or throw an error based on your requirements
        // For example, setting ret to a default value of 1.0
        ret = 1.0;
    } else {
        ret = d / (d - 2.0);
    }
    return ret;
}