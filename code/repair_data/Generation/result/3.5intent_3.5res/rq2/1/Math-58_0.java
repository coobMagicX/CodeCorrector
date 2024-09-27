public double[] fit() {
    final ParameterGuesser guesser = new ParameterGuesser(getObservations());
    final double[] guess = guesser.guess();
    final ParametricCurveFitter fitter = new ParametricCurveFitter(new Gaussian.Parametric());
    return fitter.fit(getObservations(), guess);
}