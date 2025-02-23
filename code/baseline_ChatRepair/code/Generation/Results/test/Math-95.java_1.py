protected double getInitialDomain(double p) {
    double d = getDenominatorDegreesOfFreedom();
    // Handle case where denominator degrees of freedom is less than or equal to 2
    if (d <= 2) {
        return 0.5; // or any other reasonable starting value for small degrees of freedom
    } else {
        // use mean for cases where d > 2
        return d / (d - 2.0);
    }
}
