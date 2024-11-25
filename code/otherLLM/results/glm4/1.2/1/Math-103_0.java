public double cumulativeProbability(double x) throws MathException {
    if (Double.isNaN(x) || Double.isInfinite(x)) {
        throw new IllegalArgumentException("Input value must be finite and not NaN");
    }

    if (standardDeviation <= 0.0) {
        throw new IllegalArgumentException("Standard deviation must be greater than zero");
    }

    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    return 0.5 * (1.0 + Erf.erf(z));
}

// Note: You will need to implement or have access to the Erf class and method
// as it is not a standard Java library function.