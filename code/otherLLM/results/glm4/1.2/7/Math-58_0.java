public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();

    // Check if the guessed parameters are valid before fitting
    if (Double.isNaN(guess[0]) || Double.isNaN(guess[1])) {
        throw new IllegalArgumentException("Invalid initial parameters: NaN values found.");
    }
    
    // Ensure sigma is positive, as a negative sigma does not make sense in a Gaussian fit
    if (guess[1] <= 0) {
        guess[1] = Math.abs(guess[1]); // Set to absolute value if it's non-positive
    }

    return fit(new Gaussian.Parametric(), guess);
}