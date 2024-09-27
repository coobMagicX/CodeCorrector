public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    if (guess[2] <= 0 || guess[3] <= 0) {
        guess[2] = Double.NaN;
        guess[3] = Double.NaN;
    }
    return fit(new Gaussian.Parametric(), guess);
}