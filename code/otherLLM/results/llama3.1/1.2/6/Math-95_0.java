protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    if (d <= 2.0) {
        // Handle edge case where denominator degrees of freedom is small
        throw new ArithmeticException("Denominator degrees of freedom cannot be less than or equal to 2");
    }
    ret = fd.inverseCumulativeProbability(p);
    return ret;
}