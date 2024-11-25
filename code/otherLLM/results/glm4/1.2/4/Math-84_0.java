protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    int evaluationCount = 0;

    while (true) {

        incrementIterationsCounter();

        // save the original vertex
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];

        // perform a reflection step
        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        evaluationCount++;
        
        if (comparator.compare(reflected, best) < 0) {
            // compute the expanded simplex
            final RealPointValuePair[] reflectedSimplex = simplex;
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            evaluationCount++;

            if (comparator.compare(reflected, expanded) <= 0) {
                // accept the reflected simplex
                simplex = reflectedSimplex;
            }

            return; // Optimization is successful or converged.
        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        evaluationCount++;

        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex

            // check convergence
            return; // Optimization is successful or converged.
        }

        // Check for maximum number of evaluations to avoid infinite loop.
        if (++evaluationCount > MAX_EVALUATIONS) {
            throw new OptimizationException("Maximum evaluation limit exceeded.");
        }
    }
}