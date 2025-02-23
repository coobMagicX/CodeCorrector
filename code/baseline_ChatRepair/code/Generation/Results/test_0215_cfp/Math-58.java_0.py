public double[] fit() {
    final double[] initialGuess = (new ParameterGuesser(getObservations())).guess();
    double[] correctedGuess = new double[