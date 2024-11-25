protected double getInitialDomain(double p) {
    double d = getDenominatorDegreesOfFreedom();
    if (d <= 2.0) {
        // For small degrees of freedom, use a different approach to avoid division by zero.
        return 1.0; // Default value when d is too small for accurate estimation
    } else {
        // Use mean formula for larger values of degrees of freedom
        double ret = d / (d - 2.0);
        return ret;
    }
}