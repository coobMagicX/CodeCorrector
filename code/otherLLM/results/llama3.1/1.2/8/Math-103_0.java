public double cumulativeProbability(double x) throws MathException {
    if (Double.isNaN(x)) {
        throw new MathException("Input value is NaN");
    }
    return 0.5 * (1.0 + Erf.erf((x - mean) /
            (getStandardDeviation() * Math.sqrt(2.0))));
}