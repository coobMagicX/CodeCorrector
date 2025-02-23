public double cumulativeProbability(double x) {
    if (x == Double.NEGATIVE_INFINITY) {
        return 0.0;
    } else if (x == Double.POSITIVE_INFINITY) {
        return 1.0;
    }

    // Parameter 'z' scaled for the erf function.
    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));

    // Use the error function to compute the cumulative probability.
    return 0.5 * (1.0 + Erf.erf(z));
}
