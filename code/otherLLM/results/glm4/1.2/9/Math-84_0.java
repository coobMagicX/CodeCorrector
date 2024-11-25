protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    final int MAX_ITERATIONS = 150; // Maximum number of iterations allowed

    while (getIterationsCounter() < MAX_ITERATIONS) {
        incrementIterationsCounter();

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
            continue; // Continue to the next iteration after accepting new simplex

        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = reflectedSimplex; // Assign the expanded simplex to simplex array
        } else {
            // check convergence
            break; // Break the loop if no improvement is found within the allowed number of iterations
        }
    }

    // Optionally, handle the case where the optimizer has reached the maximum number of iterations without convergence
    if (getIterationsCounter() >= MAX_ITERATIONS) {
        throw new OptimizationException("Maximum number of iterations reached without convergence.");
    }
}