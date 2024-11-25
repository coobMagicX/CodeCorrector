public double cumulativeProbability(double x) throws MathException {
    if (x < -Double.MAX_VALUE / 2 || x > Double.MAX_VALUE / 2) {
        throw new MathException("Input value is out of range.");
    }
    return 0.5 * (1.0 + Erf.erf((x - mean) /
            (getStandardDeviation() * Math.sqrt(2.0))));
}