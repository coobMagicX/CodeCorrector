public double cumulativeProbability(double x) {
    double mean = 0; // Example mean value
    double standardDeviation = 1; // Example standard deviation value
    return 0.5 * (1.0 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0))));
}