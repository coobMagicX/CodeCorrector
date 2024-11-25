public double[] fit() {
    // Check if sigma values are negative and throw exception if true
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    if (guess[2] < 0 || guess[3] < 0) { // assuming guess array has [mean, sigma1, sigma2, ...]
        throw new IllegalArgumentException("Sigma values cannot be negative");
    }
    return fit(new Gaussian.Parametric(), guess);
}