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
            // First compare reflected to expanded
            if (comparator.compare(reflected, expanded) <= 0) {
                // accept the reflected simplex, reflected result is better or equal to expanded
                simplex = copySimplex(reflectedSimplex); // Make sure to implement or define copySimplex appropriately
            } else {
                // accept the expanded simplex
                simplex = copySimplex(expansionSimplex);
            }
            return;
        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = copySimplex(contractionSimplex); // Make sure to implement or define copySimplex appropriately
        } else {
            // accept the original simplex (possible shrinking), if contraction was not successful
            simplex = copySimplex(original);
        }
        return;
    }
}
