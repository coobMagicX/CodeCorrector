protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    
    // handle division by zero when d is 2.0
    if (d == 2.0) {
        // handle this specific case based on mathematical context
        // set ret to a default value or throw an error
        // example: ret = 0.0; or throw new IllegalArgumentException("Invalid denominator degrees of freedom");
    } else {
        ret = d / (d - 2.0);
    }
    
    return ret;
}