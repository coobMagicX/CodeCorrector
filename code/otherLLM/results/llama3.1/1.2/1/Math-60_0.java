public double inverseCumulativeProbability(final double p) throws MathException {
    if (p == 0 || p == 1) { // Simplified conditionals
        return Double.isNaN(p) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
    }
    
    // ...
}