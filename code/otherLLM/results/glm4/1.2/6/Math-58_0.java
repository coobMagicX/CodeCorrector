public double[] fit() {
    // Ensure that there are observations before fitting, to avoid NaN or Infinities
    if (getObservations().length == 0) {
        throw new IllegalArgumentException("No data points available for fitting.");
    }

    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    
    // Check for numerical stability issues with the guessed parameters
    if (Double.isNaN(guess[1]) || Double.isNaN(guess[2])) {
        throw new ArithmeticException("Guessed parameters lead to numerical instability.");
    }
    
    // Ensure that sigma is not zero or negative, as this would be invalid for a Gaussian function
    if (guess[2] <= 0.0) {
        throw new IllegalArgumentException("Sigma value must be positive.");
    }

    try {
        return fit(new Gaussian.Parametric(), guess);
    } catch (Exception e) {
        // Handle any exceptions from the fit method to ensure stability of the overall process
        System.err.println("An error occurred during fitting: " + e.getMessage());
        throw new RuntimeException("Fitting failed due to an unexpected error.", e);
    }
}