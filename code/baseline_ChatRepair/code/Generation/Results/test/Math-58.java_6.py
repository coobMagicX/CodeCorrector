public double[] fit() {
    final double[] initialGuess = (new ParameterGuesser(getObservations())).guess();
    // Ensure that the initial guess values are strictly positive where required
    double[] safeGuess = new double[initialGuess.length];
    for (int