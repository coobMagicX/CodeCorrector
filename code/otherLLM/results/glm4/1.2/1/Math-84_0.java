protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    int evaluationCount = 0; // Initialize a counter for the number of evaluations
    final int maxEvaluations = ...; // Define the threshold for maximum allowed evaluations

    while (true) {
        incrementIterationsCounter();
        evaluationCount++; // Increment the evaluation count after each iteration

        if (evaluationCount > maxEvaluations) { // Check if the evaluation limit is exceeded
            throw new OptimizationException("Evaluation limit exceeded");
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
                continue; // Continue to the next iteration without returning
            }

        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex

            // check convergence
            return; // Exit the loop after accepting a better solution
        }
    }
}