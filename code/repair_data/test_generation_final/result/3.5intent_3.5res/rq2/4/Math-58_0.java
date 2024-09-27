public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    // Check if any of the guess parameters are negative, and replace them with NaN
    for (int i = 0; i < guess.length; i++) {
        if (guess[i] < 0) {
            guess[i] = Double.NaN;
        }
    }
    
    // Add a check for NaN values in the guess parameters
    for (int i = 0; i < guess.length; i++) {
        if (Double.isNaN(guess[i])) {
            return new double[0]; // Return an empty array if any parameter is NaN
        }
    }
    
    return fit(new Gaussian.Parametric(), guess);
}