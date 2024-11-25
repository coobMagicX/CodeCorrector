protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    
    // Corrected calculation logic for getInitialDomain
    if (d > 2.0) {
        ret = d / (d - 2.0);
    } else if (d == 2.0) {
        // Handle the case when denominator degrees of freedom is exactly 2.0
        ret = Double.POSITIVE_INFINITY;
    } else {
        throw new ArithmeticException("Denominator degrees of freedom cannot be less than 2.0");
    }
    
    return ret;
}