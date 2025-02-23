public double[] fit() {
    final double[] initialGuess = (new ParameterGuesser(getObservations())).guess();
    double[] correctedGuess = new double[initialGuess.length];

    // Assume initialGuess follows a certain index pattern: e.g.,
    // index 0 could be mean, index 1 could be standard deviation, index 2 could be scale if applicable
    correctedGuess[0] = initialGuess