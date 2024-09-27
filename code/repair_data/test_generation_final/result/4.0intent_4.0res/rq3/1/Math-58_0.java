public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    Gaussian.Parametric model = new Gaussian.Parametric();
    return fit(model, guess);
}