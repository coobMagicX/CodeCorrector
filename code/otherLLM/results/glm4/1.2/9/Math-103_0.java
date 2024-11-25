public double cumulativeProbability(double x) throws MathException {
    if (mean == 0 && standardDeviation == 0) {
        throw new MathException("Mean and standard deviation must be set before calculating cumulative probability.");
    }
    
    try {
        return 0.5 * (1.0 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0))));
    } catch (ArithmeticException e) {
        // Log the error or handle it according to your application's needs
        throw new MathException("Error calculating cumulative probability", e);
    }
}