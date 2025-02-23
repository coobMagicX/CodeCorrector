protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    while (true) {

        incrementIterationsCounter();

        // save the original vertex
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];

        // Perform a reflection step
        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        if (comparator.compare(reflected, best) < 0) {
            // Compute the expanded simplex
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(expanded, reflected) < 0) {
                simplex = simplex; // assign expanded simplex if it's better than reflected
            } else {
                simplex = original; // only accept the reflected simplex if not expanding
            }
            return; // exit as we accept the reflected or expanded simplex
        }

        // Compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            simplex = simplex; // accept the contracted simplex
            return; // exit as we accept the contracted simplex
        } else {
            throw new OptimizationException("Contraction did not produce a better point, indicating possible cycling; aborting to prevent infinite loop");
        }
    }
}
