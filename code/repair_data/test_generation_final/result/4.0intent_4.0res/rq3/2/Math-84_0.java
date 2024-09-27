protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    while (true) {

        incrementIterationsCounter();

        // save the original vertex
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];

        // perform a reflection step
        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        if (comparator.compare(reflected, best) < 0) {

            // compute the expanded simplex
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(expanded, reflected) < 0) {
                // accept the expanded simplex
                simplex = evaluateNewSimplex(original, khi, comparator);
            } else {
                // accept the reflected simplex
                simplex = evaluateNewSimplex(original, 1.0, comparator);
            }

            return;

        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = evaluateNewSimplex(original, gamma, comparator);
        } else {
            // all reflected, expanded, and contracted steps failed, shrink the simplex
            simplex = evaluateNewSimplex(original, sigma, comparator);
        }

        // check convergence
        if (checkConvergence()) {
            return;
        }
    }

}