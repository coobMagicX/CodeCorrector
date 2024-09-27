public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    return fit(new Gaussian.Parametric(), guess, 1e-6);
}