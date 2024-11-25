public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    
    // Handle negative sigma values
    Gaussian.Parametric param = new Gaussian.Parametric();
    if (param.sigma < 0) {
        throw new IllegalArgumentException("Sigma value cannot be negative");
    }
    
    return fit(param, guess);
}