public double cumulativeProbability(double x) {
    if (x == Double.NEGATIVE_INFINITY) {
        return 0.0;
    } else if (x == Double.POSITIVE_INFINITY) {
        return 1.0;
    }

    double dev = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    
    // Check for extreme dev values which may cause numerical issues
    if (dev > 10) {
