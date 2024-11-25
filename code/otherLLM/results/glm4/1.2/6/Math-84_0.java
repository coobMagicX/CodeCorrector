protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    final int maxIterations = 1000; // Example of a maximum number of iterations

    for (int iteration = 0; iteration < maxIterations; incrementIterationsCounter()) {
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
            }
        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex

            // check convergence - replace this line with proper convergence criteria
            if (isConverged(best)) { // Assuming isConverged() checks for convergence criteria
                return;
            }
        }
    }

    throw new OptimizationException("Maximum number of iterations exceeded");
}

// Method to check for convergence (example method, needs to be implemented based on actual convergence criteria)
private boolean isConverged(RealPointValuePair best) {
    // Convergence logic goes here
    // Return true if the simplex has converged, false otherwise
}