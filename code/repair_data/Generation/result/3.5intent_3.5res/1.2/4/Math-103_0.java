public double cumulativeProbability(double x) throws MathException {
    setMean(0); // Set the mean to 0
    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0)); // Calculate the z-score
    double cumulativeProbability;
    
    if (z > 6) {
        cumulativeProbability = 1.0; // If z is greater than 6, set the cumulative probability to 1
    } else if (z < -6) {
        cumulativeProbability = 0.0; // If z is less than -6, set the cumulative probability to 0
    } else {
        cumulativeProbability = 0.5 * (1.0 + Erf.erf(z)); // Calculate the cumulative probability using the z-score
    }
    
    return cumulativeProbability;
}