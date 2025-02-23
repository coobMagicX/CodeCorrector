public double cumulativeProbability(double x) throws MathException {
    // Using 40 times the standard deviation as the cutoff might not be sufficient in all cases.
    // Adjust these cutoff thresholds based on practical observation or further analysis if needed.
    final double cutoffLower = mean - 8 * standardDeviation;
    final double cutoffUpper = mean + 8 * standardDeviation;

    if (x <= cutoffLower) {
       