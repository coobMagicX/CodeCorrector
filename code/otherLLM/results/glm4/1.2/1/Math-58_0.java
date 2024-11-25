public double[] fit() {
    // Ensure getObservations returns non-null data and is not empty.
    if (getObservations() == null || getObservations().length == 0) {
        throw new IllegalArgumentException("No observations provided for fitting.");
    }

    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    
    // Check if the guess array is non-null and has the correct length.
    if (guess == null || guess.length != Gaussian.Parametric.PARAMETER_COUNT) {
        throw new IllegalArgumentException("Invalid or incorrect number of guessed parameters.");
    }

    // Call fit with a new instance of Gaussian.Parametric to avoid shared state issues.
    double[] fittedParameters = fit(new Gaussian.Parametric(), guess);
    
    return fittedParameters;
}