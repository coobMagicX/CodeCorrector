public double[] fit() {
    if (getObservations().length < 2) {
        throw new IllegalArgumentException("At least two observations are required for fitting.");
    }
    
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    return fit(new Gaussian.Parametric(), guess);
}