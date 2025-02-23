protected final double doSolve() {
    // Define the maximum number of evaluations.
    int maxEvaluations = getMaxEvaluations();
    int evaluations = 0;

    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluations++;
    double f1 = computeObjectiveValue(x1);
    evaluations++;

    // Add a check right after each computation of the objective value
    if (evaluations > maxEvaluations) {
        throw new TooManyEvaluationsException(maxEvaluations);
    }

    // Rest of the method follows ...
    while (true) {
        // Calculate the next approximation.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++;

        // Check evaluations count after each computeObjectiveValue call.
        if (evaluations > maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        // Rest of the loop follows ...
    }
}
