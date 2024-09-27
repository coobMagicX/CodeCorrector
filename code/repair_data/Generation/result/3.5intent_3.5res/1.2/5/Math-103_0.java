public double cumulativeProbability(double x) throws MathException {
    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    double result = 0.5 * (1.0 + Erf.erf(z));
    
    // Check for large negative values of x
    if (z < -6.0) {
        result = 0.0;
    }
    // Check for large positive values of x
    else if (z > 6.0) {
        result = 1.0;
    }
    
    // Check for very small positive values of x
    else if (z < 1e-10) {
        result = 0.0;
    }
    
    return result;
}