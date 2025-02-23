public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    
    // Ensure that the guessed standard deviation is positive.
    if (guess[1] <= 0) {
        guess[1] = Math.abs(guess[1]) + 0.1;
    }

    return fit(new Gaussian.Parametric(),