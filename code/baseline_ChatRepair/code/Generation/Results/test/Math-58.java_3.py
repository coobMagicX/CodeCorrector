public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    
    // Assuming guess[0] is mean, guess[1] is standard deviation
    if (guess.length