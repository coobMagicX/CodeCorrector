public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    if (Double.isNaN(guess[0]) || Double.isInfinite(guess[0])) {
        throw new RuntimeException("Guess contains NaN or infinite value");
    }
    return fit(new Gaussian.Parametric(), guess);
}