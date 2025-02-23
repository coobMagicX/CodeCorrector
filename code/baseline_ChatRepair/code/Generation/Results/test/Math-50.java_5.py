protected final double doSolve() {
    final int maxEval = getMaxEvaluations();  // This needs to be defined or accessed accordingly
    int evalCount = 0;

    // -- Existing code before this point remains unchanged --

    while (true) {
        evalCount++;
        if (evalCount > maxEval) {
            throw new TooManyEvaluationsException(maxEval);
        }

        // Calculate the next approximation
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evalCount++; // Update evaluation count after each function evaluation

        // -- Rest of the existing loop code remains essentially unchanged --
    }
}
