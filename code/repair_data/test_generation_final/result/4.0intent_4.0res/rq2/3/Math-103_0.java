public double cumulativeProbability(double x) throws MathException {
    // Adjusting to handle extremely large or small values of x more accurately
    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    if (z > 6) {
        return 1.0; // Return 1.0 for extreme high values, as erf function approaches 1
    } else if (z < -6) {
        return 0.0; // Return 0.0 for extreme low values, as erf function approaches -1
    }
    return 0.5 * (1.0 + Erf.erf(z));
}