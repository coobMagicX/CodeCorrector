protected double getInitialDomain(double p) {
    double d = getDenominatorDegreesOfFreedom();
    if (d > 2.0) {
        return d / (d - 2.0);
    } else {
        // Handle the case when `d` is less than or equal to 2 to avoid division by zero or negative denominator
        return Double.NaN; // Return NaN (Not a Number) or consider throwing an exception or another appropriate value
    }
}