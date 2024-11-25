public double chiSquare(double[] expected, long[] observed)
throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException(
                "observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException(
            "observed counts must be non-negative and expected counts must be positive");
    }
    
    double sumSq = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        // Use double casting to avoid integer overflow
        long diff = observed[i] - (long)expected[i];
        double dev = ((double)diff * diff);
        
        // Cast expected value to double and check for division by zero
        if(expected[i] == 0.0d) {
            throw new ArithmeticException("Division by zero in chiSquare calculation");
        }
        
        sumSq += dev / expected[i];
    }
    
    return sumSq;
}