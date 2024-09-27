protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    if (d > 2.0) {
        // Use mean
        ret = d / (d - 2.0);
    } else if (d > 0.0) {
        // Handle small degrees of freedom where d <= 2
        ret = d / 2.0; // Simple adjustment for small degrees of freedom
    } else {
        throw new IllegalArgumentException("Degrees of freedom must be positive.");
    }
    return ret;
}