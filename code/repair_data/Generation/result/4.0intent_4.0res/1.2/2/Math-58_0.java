public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    // Ensure that the initial guess for sigma (standard deviation) is positive
    if (guess[2] <= 0) {
        guess[2] = 0.1; // Set to a small positive value if guessed sigma is non-positive
    }
    return fit(new Gaussian.Parametric(), guess);
}