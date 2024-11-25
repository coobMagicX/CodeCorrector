public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    if(guess[1] < 0) { // Check for negative sigma value
        throw new RuntimeException("Sigma value cannot be negative");
    }
    return fitter.fit(new Gaussian.Parametric(), guess); // Use the correct optimizer implementation
}