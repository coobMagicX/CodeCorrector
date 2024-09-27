protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    if (d <= 2.0) {
        // Handle the case where denominator degrees of freedom is less than or equal to 2
        // As division by zero would occur or result in an extremely high value, set ret to a default or meaningful value
        // Here, setting to a small positive value as a simple solution
        ret = 1.0; // This value should be chosen based on the application context
    } else {
        // use mean
        ret = d / (d - 2.0);
    }
    return ret;
}