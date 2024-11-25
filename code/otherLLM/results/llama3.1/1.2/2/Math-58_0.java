public double[] fit() {
    final double[] guess;
    try {
        guess = new ParameterGuesser(getObservations()).guess();
    } catch (NullArgumentException e) {
        throw e; // rethrow the exception if input is invalid
    }

    // Check for NaN values before attempting to fit the model
    if (Double.isNaN(guess[0]) || Double.isNaN(guess[1])) {
        throw new RuntimeException("Invalid initial guess: contains NaN value");
    }

    return fit(new Gaussian.Parametric(), guess);
}