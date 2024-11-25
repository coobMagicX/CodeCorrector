public double cumulativeProbability(double x) throws MathException {
    if (Double.isNaN(x)) { // Check for NaN values
        throw new MathException("Input value is NaN");
    }
    
    return 0.5 * (1.0 + Erf.erf((x - mean) / 
            (standardDeviation > 0 ? standardDeviation : Double.MIN_VALUE)));
}