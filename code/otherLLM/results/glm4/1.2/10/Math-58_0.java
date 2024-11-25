public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();

    // Check if the guessed parameters are valid; if not, use a default set of guesses.
    if (!isValidGuess(guess)) {
        guess = new double[]{0.1, 1.0}; // Default values for mean and sigma
    }

    return fit(new Gaussian.Parametric(), guess);
}

// Helper method to check the validity of guessed parameters.
private boolean isValidGuess(double[] guess) {
    if (guess == null || guess.length != 2) {
        return false;
    }
    double sigma = guess[1];
    double mean = guess[0];

    // Check for numerical instability or precision issues
    if (sigma <= 0.0) {
        return false;
    }
    if (Double.isNaN(mean) || Double.isInfinite(mean)) {
        return false;
    }

    return true;
}