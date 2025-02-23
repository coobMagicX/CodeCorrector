public double[] fit() {
    final double[] initialGuess = (new ParameterGuesser(getObservations())).guess();
    double[] correctedGuess = new double[initialGuess.length];

    // Ensure positive values for parameters that require positivity
    // (typically, mean can be anything, second parameter is standard deviation which must be > 0)
    correctedGuess[0] = initialGuess[0]; // mean is typically unrestricted
    correctedGuess[1] = Math.max(initialGuess