protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    if (d > 2.0) {
        // use mean
        ret = d / (d - 2.0);
    } else if (d > 0) {
        // When degrees of freedom are less than or equal to 2 but positive, approximate with a small positive value
        ret = 0.1; // This is an arbitrary small domain value to avoid division by zero or negative values
    } else {
        throw new IllegalArgumentException("Denominator degrees of freedom must be positive.");
    }
    return ret;
}