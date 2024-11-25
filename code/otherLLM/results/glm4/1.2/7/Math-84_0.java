protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    final int maxEvaluations = 150; // Maximum number of allowed evaluations
    int evaluations = 0;

    while (true) {
        incrementIterationsCounter();
        evaluations++;

        if (evaluations > maxEvaluations) { // Check the evaluation limit
            throw new OptimizationException("Maximum number of evaluations exceeded.");
        }

        // save the original vertex
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];

        // perform a reflection step
        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        if (comparator.compare(reflected, best) < 0) {
            // compute the expanded simplex
            final RealPointValuePair[] reflectedSimplex = simplex;
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(reflected, expanded) <= 0) {
                // accept the reflected simplex
                simplex = reflectedSimplex;
                continue; // Continue to iterate since we have a better candidate
            }
        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = original; // Update the simplex to the best candidate found so far

            // Check convergence before returning
            if (isConverged(simplex, comparator)) { // Assuming isConverged exists and checks for convergence
                return;
            }
        } else {
            // If contracted simplex did not improve, check convergence
            if (isConverged(simplex, comparator)) {
                return;
            }
        }
    }
}

// Assuming this method exists to check for convergence
private boolean isConverged(final RealPointValuePair[] simplex, final Comparator<RealPointValuePair> comparator) {
    // Implement your convergence criteria here.
    // For example, a possible simple criterion could be:
    // Check if the difference between the best and worst values in the current simplex is below some threshold.
}