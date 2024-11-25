protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();

    if (d <= 2.0) {
        // For small degrees of freedom, use an alternative formula to avoid division by zero or infinite result.
        ret = Math.sqrt(2 * Math.PI / d) * Math.pow(p, d / 2.0) * Math.exp(-p);
    } else {
        // Use the original formula for larger degrees of freedom.
        ret = d / (d - 2.0);
    }
    
    return ret;
}