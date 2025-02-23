protected final double doSolve() throws TooManyEvaluationsException {
    // Maximum number of allowed evaluations
    final int maxEval = getMaxEvaluations(); // Assume this method is implemented to return the maximum evaluations allowed.
    int evalCount = 0;

    // existing code unchanged until the while loop
    while (true) {
        // Check if maximum evaluations exceeded
        if (evalCount >= maxEval) {
            throw new TooManyEvaluationsException(maxEval);
        }

        // Calculate the next approximation.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evalCount++;  // Increment the evaluation counter each time the objective function is computed

        // remaining unchanged code from your provided method

        // end of while loop
    }

    // end of method
}
