public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    
    // Ensure that the initial guess is non-negative. If sigma is negative, set a safe default.
    if (guess[1] < 0) { // Assuming the second element of the guess array is sigma
        guess[1] = Math.max(0.0001, guess[1]); // Set to a small positive value
    }
    
    return fit(new Gaussian.Parametric(), guess);
}