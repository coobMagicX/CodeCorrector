protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    while (!isConverged()) { // Adjusted to check for convergence instead of an infinite loop

        incrementIterationsCounter();

        // save the original vertex
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];

        // perform a reflection step
        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        if (comparator.compare(reflected, best) < 0) {

            // compute the expanded simplex
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(reflected, expanded) <= 0) {
                // accept the reflected simplex
                simplex = new RealPointValuePair[]{reflected}; // Fix to properly update simplex with reflected point
            } else {
                // accept the expanded simplex
                simplex = new RealPointValuePair[]{expanded}; // Fix to properly update simplex with expanded point
            }
            continue; // Continue the loop to further optimize
        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = new RealPointValuePair[]{contracted}; // Fix to properly update simplex with contracted point
        } else {
            // reduce the simplex around the best point
            simplex = reduceSimplex(simplex); // Adding a reduction step if no improvement
        }

        // check convergence
        if (isConverged()) {
            return; // Exit the loop if converged
        }
    }
}

// Additional methods used in the fix (assuming their existence)
private boolean isConverged() {
    // Method to determine if the optimization has converged
    return someConvergenceCriteria();
}

private RealPointValuePair[] reduceSimplex(RealPointValuePair[] simplex) {
    // Method to reduce the size of the simplex around the best point
    return someReductionMethod(simplex);
}