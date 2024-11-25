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
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(reflected, expanded) <= 0) {
                // accept the reflected simplex
                simplex = reflectedSimplex;
            }

            return;

        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = Arrays.copyOf(simplex, original.length);
            simplex[0] = contracted;
        } else {
            // check convergence
            if (converged(best, simplex)) {
                return;
            }
        }

    }

}

private boolean converged(final RealPointValuePair best, final RealPointValuePair[] simplex)
throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    for (int i = 0; i < simplex.length - 1; i++) {
        if (comparator.compare(simplex[i], best) != 0) {
            return false;
        }
    }

    return true;

}