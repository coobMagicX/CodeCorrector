public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    return fit(new PositiveSigmaGaussian.Parametric(), guess);
}

public static class PositiveSigmaGaussian implements ParametricUnivariateFunction {
    @Override
    public double value(double x, double... p) {
        double a = p[0];
        double mean = p[1];
        double sigma = Math.max(p[2], 0.0); // Constrain sigma to positive values
        return a * Math.exp(-0.5 * ((x - mean) / sigma) * ((x - mean) / sigma));
    }

    @Override
    public double[] gradient(double x, double... p) {
        double a = p[0];
        double mean = p[1];
        double sigma = Math.max(p[2], 0.0); // Constrain sigma to positive values

        double factor = (x - mean) / sigma;
        double expFactor = Math.exp(-0.5 * factor * factor);

        double dAdx = expFactor;
        double dMdx = a * expFactor * factor / sigma;
        double dSdx = a * expFactor * factor * factor / (sigma * sigma);

        return new double[]{dAdx, dMdx, dSdx};
    }
}