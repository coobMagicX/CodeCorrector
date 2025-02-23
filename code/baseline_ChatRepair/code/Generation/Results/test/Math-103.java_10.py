public double cumulativeProbability(double x) throws MathException {
    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    if (z < -10) {
        // If z-score is extremely low, the cumulative probability approaches 0
        return 0.0;
    } else if (z > 10) {
        // If z-score is extremely high, the cumulative probability approaches 1
        return 1.0;
    } else {
        // Use the error function computation for normal cases
       