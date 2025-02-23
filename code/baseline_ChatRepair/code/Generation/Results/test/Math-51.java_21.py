protected final double doSolve() {
    final int MAX_EVALUATIONS = 3624; // Update this if you have a specific maximum
    int evaluations = 0;

    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluations++;
    double f1 = computeObjectiveValue(x1);
    evaluations++;

    ...

    while (true) {
        // Must stop if no further evaluations are allowed
        if (evaluations >= MAX_EVALUATIONS) {
            throw new TooManyEvaluationsException(MAX_EVALUATIONS);
        }

        // Calculate the next approximation.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++;

        ...

        // Check if x0 and x1 are converging
        if (Math.abs(x1 - x0) < atol || Math.abs(f1) < ftol) {
            // Check return conditions depending on the patience
            ...

            // If no satisfactory x found, throw exception due to convergence failure
            if (evaluations >= MAX_EVALUATIONS) {
                throw new ConvergenceException();
            }
        }

        // Update the bounds
        ...
    }
}
