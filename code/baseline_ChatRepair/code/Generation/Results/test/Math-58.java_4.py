public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    
    // Assumption: guess[0] is mean, guess[1] is standard deviation which must be positive
    if (guess[1] <= 0) {
        guess[1] = 0.1;  // Assign a small positive value if guessed standard deviation is not positive
