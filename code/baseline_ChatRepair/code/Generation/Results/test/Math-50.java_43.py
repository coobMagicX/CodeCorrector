protected final double doSolve() {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    int maxEvaluations = getMaxEvaluations(); // assuming there's a function to get the maximum allowed evaluations
    int evaluations = 0;  // counter for the number of evaluations

    // Evaluate initial points
    double f0 = computeObjectiveValue(x0); evaluations++;
    if (evaluations >= maxEvaluations) {
        throw new TooManyEvaluationsException(maxEvaluations);
    }
    double f1 = computeObjectiveValue(x1); evaluations++;
    if (evaluations >= maxEvaluations) {
        throw new TooManyEvaluationsException(maxEvaluations);
    }

    // If one of the bounds is the exact root, return it.
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    // Verify bracketing of initial solution.
    verifyBracketing(x0, x1);

    // Get accuracies.
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;

    while (true) {
        // Calculate the next approximation.
        double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        double fx = computeObjectiveValue(x); evaluations++;
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        // Check if the new approximation is an exact root
        if (fx == 0.0) {
            return x;
        }

        // [Additional logic retained from original function...]

        // Check consistency of evaluation counter
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        // [End of function logic before while loop]
    }
}
