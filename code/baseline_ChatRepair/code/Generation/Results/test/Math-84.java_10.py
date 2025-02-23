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
            if (comparator.compare(expanded, best) < 0) {
                // accept the expanded simplex if the expansion provides better result
                simplex = replaceWorst(original, expanded);
                return;
            } else {
                // accept the reflected simplex instead since expansion is not better 
                simplex = replaceWorst(original, reflected);
                return;
            }
        }

        // if reflection did not yield a new best, compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // if contraction is an improvement, accept the contracted simplex
            simplex = replaceWorst(original, contracted);
        } else {
            // if contraction also fails to produce a better result, consider shrinking the whole simplex
            simplex = shrinkSimplex(original);
        }
        // Check convergence after attempting different tactics (reflection, expansion, and contraction)
        // Other conditions/convergence tests would be prudent here to break the loop
        return;
    }
}

private RealPointValuePair[] replaceWorst(RealPointValuePair[] simplex, RealPointValuePair replacement) {
    // Implement how to replace the worst point in the simplex with a newer, better point (replacement)
    // Details would depend on the ordering and structure of the simplex
    return simplex;  // Placeholder for actual implementation
}

private RealPointValuePair[] shrinkSimplex(RealPointValuePair[] simplex) {
    // Implement the shrinking strategy for the simplex
    // Typically, this reduces all non-best vertices toward the best vertex
    return simplex;  // Placeholder for actual implementation
}
