public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();

    // Ensuring that the initial guess for sigma (standard deviation) is positive
    if (guess[2] <= 0) {
        guess[2] = 0.1; // Set a minimum positive threshold for sigma if negative or zero
    }

    return fit(new Gaussian.Parametric(), guess);
}