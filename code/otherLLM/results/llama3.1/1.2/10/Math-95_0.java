protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    
    // use mean
    if (d >= 2.0) {
        ret = d / (d - 2.0);
    } else {
        // If denominator degrees of freedom is less than 2, 
        // we can't divide by zero in the formula.
        // We'll just return a default value or throw an exception,
        // depending on your requirements.
        throw new ArithmeticException("Denominator degrees of freedom must be at least 2.");
    }
    
    return ret;
}