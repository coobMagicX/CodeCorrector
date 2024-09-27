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
                // if expanded is better than reflected, accept expanded
                simplex = new RealPointValuePair[] {expanded};
            } else {
                // otherwise, accept reflected
                simplex = new RealPointValuePair[] {reflected};
            }

            // check convergence
            if (checkConvergence()) {
                return;
            }

        } else {
            // compute the contracted simplex
            final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
            if (comparator.compare(contracted, best) < 0) {
                // accept the contracted simplex
                simplex = new RealPointValuePair[] {contracted};
            } else {
                // if contraction is not better, use shrink method
                shrinkSimplex();
            }

            // check convergence
            if (checkConvergence()) {
                return;
            }
        }
    }
}