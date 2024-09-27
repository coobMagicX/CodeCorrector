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
            final RealPointValuePair[] reflectedSimplex = simplex;
            final RealPointValuePair expanded = evaluateNewSimplex(reflectedSimplex, khi, comparator);
            if (comparator.compare(reflected, expanded) <= 0) {
                // accept the reflected simplex
                simplex = reflectedSimplex;
            } else {
                // accept the expanded simplex
                simplex = new RealPointValuePair[] {expanded};
            }

            return;

        } else {
            // compute the contracted simplex
            final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
            if (comparator.compare(contracted, best) < 0) {
                // accept the contracted simplex
                simplex = new RealPointValuePair[] {contracted};
            } else {
                // shrink the simplex
                final RealPointValuePair[] shrunkSimplex = evaluateNewSimplex(original, rho, comparator);
                simplex = shrunkSimplex;
                if (iterationsCounter > maxIterations) {
                    throw new OptimizationException("Max iterations exceeded");
                }
            }
        }

        // check convergence
        if (checkConvergence()) {
            return;
        }
    }
}