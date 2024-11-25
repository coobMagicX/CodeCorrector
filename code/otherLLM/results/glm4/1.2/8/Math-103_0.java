public double cumulativeProbability(double x) throws MathException {
    if (Double.isNaN(x) || Double.isInfinite(x)) {
        throw new MathException("Input value is NaN or Infinite");
    }

    // Using the error function for the standard normal distribution
    double z = (x - this.mean) / this.getStandardDeviation();
    
    // Clamp the result to be within the valid range of cumulative probability
    return Math.min(Math.max(0.0, 0.5 * (1.0 + Erf.erf(z))), 1.0);
}