public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        double z = dev / (getStandardDeviation() * FastMath.sqrt(2.0));
        double probability = Erf.erf(z);
        
        if (probability <= 0.0) {
            return 0.0;
        } else if (probability >= 1.0) {
            return 1.0;
        } else {
            return 0.5 * (1.0 + probability);
        }
    } catch (MaxIterationsExceededException ex) {
        if (x < (mean - 20 * getStandardDeviation())) {
            return 0;
        } else if (x > (mean + 20 * getStandardDeviation())) {
            return 1;
        } else {
            throw ex;
        }
    }
}