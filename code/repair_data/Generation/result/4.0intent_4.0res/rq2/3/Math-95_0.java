protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    if (d <= 2.0) {
        // Adjusting domain for small degrees of freedom to handle edge cases including d = 2
        ret = Double.MAX_VALUE; // Choosing a large finite value to avoid division by zero or unrealistic results
    } else {
        // use mean
        ret = d / (d - 2.0);
    }
    return ret;
}