public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    // Ensure that the initial guess for sigma (standard deviation) is positive
    if (guess[2] <= 0) {
        guess[2] = 0.1; // Provide a small positive value if initial sigma is non-positive
    }
    return fit(new Gaussian.Parametric(), guess);
}