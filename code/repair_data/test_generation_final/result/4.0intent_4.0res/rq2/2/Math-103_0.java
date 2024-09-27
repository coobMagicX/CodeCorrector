public double cumulativeProbability(double x) throws MathException {
    if (standardDeviation == 0) throw new MathException("Standard deviation cannot be zero.");
    if (x - mean > 40 * standardDeviation) return 1.0; // Handles extreme high values
    if (x - mean < -40 * standardDeviation) return 0.0; // Handles extreme low values
    return 0.5 * (1.0 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0))));
}