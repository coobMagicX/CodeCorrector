public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();

    // Check if sigma is negative and handle it properly
    if (guess[1] < 0) {
        throw new IllegalArgumentException("Sigma cannot be negative");
    }

    return fit(new Gaussian.Parametric(), guess);
}