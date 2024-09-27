public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    // Ensure that sigma is positive before fitting
    if (guess[2] <= 0) {
        guess[2] = Double.NaN;
    }
    return fit(new Gaussian.Parametric(), guess);
}