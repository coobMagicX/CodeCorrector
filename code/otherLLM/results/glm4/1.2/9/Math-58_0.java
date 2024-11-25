public double[] fit() {
    // Check if observations are within a valid range to avoid numerical instability
    if (getObservations().length == 0) {
        throw new IllegalArgumentException("No data points available.");
    }
    
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    
    // Validate the guess parameters before fitting
    for (double param : guess) {
        if (Double.isNaN(param) || Double.isInfinite(param)) {
            throw new IllegalArgumentException("Parameter values are not valid.");
        }
    }

    return fit(new Gaussian.Parametric(), guess);
}