public double cumulativeProbability(double x) throws MathException {
    
    if (Double.isInfinite(x)) {
        return x < 0 ? 0.0 : 1.0;
    }

    
    double cutOff = 20 * standardDeviation; 
    if (x < mean - cutOff) {
        return 0.0;
    } else if (x > mean + cutOff) {
        return 1.0;
    }

    
    final double dev = x - mean;
    try {
        
        double normalizedVal = dev / (standardDeviation * Math.sqrt