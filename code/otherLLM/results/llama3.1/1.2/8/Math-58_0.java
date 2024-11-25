public double[] fit() {
    if (getObservations().length < 2) { // Check for edge case where input data has less than 2 points
        throw new RuntimeException("At least two observations are required to fit a distribution.");
    }
    
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    return fit(new Gaussian.Parametric(), guess);
}