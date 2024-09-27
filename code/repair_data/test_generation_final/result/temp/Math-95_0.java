protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    
    if (d <= 2.0) {
        // Handle the case where denominator degrees of freedom is less than or equal to 2
        throw new IllegalArgumentException("Degrees of freedom must be greater than 2 for the F-distribution.");
    } else {
        // use mean
        ret = d / (d - 2.0);
    }
    
    return ret;
}