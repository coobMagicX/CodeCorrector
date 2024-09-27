protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    
    if (d == 2.0) {
        // handle division by zero error
        // set ret to a default value or throw an error
        // For example, set ret to -1.0
        ret = -1.0;
    } else if (d <= 1.0) {
        // handle invalid degrees of freedom
        // set ret to a default value or throw an error
        // For example, set ret to -1.0
        ret = -1.0;
    } else {
        ret = d / (d - 2.0);
    }
    
    return ret;
}