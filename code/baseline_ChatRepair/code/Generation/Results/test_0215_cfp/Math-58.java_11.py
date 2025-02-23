public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    // Ensure all guessed parameters are strictly positive
    for (int i = 0; i < guess.length; i++) {
        if (guess[i] <= 0) {
            guess[i] = 0.1; // Set a minimal positive value if guessed value is non