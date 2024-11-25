protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    int evaluationCount = 0;

    while (true) {
        
        incrementIterationsCounter();
        evaluationCount++;

        // save the original vertex
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];

        // perform a reflection step
        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        if (comparator.compare(reflected, best) < 0) {
            evaluationCount += 2; // Include the expanded simplex evaluation

            // compute the expanded simplex
            final RealPointValuePair[] reflectedSimplex = simplex;
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(reflected, expanded) <= 0) {
                // accept the reflected simplex
                simplex = reflectedSimplex;
            }

            // Check for convergence or maximum evaluation count
            if (evaluationCount > 150 || checkConvergence(simplex)) {
                return;
            }
        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            evaluationCount += 1; // Include the contracted simplex evaluation

            // accept the contracted simplex
            // check convergence
            if (evaluationCount > 150 || checkConvergence(simplex)) {
                return;
            }
        }

    }
}

private boolean checkConvergence(RealPointValuePair[] simplex) {
    // Placeholder for actual convergence check, should be implemented based on specific optimization needs.
    // This is a simple example that returns false (not converged).
    return false;
}