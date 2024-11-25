protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    final int maxEvaluations = getEvaluations();
    int evaluations = 0;

    while (evaluations < maxEvaluations) {
        
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
            
            evaluations++; // Increment evaluation count after accepting a new simplex

        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            
            evaluations++; // Increment evaluation count after accepting a new simplex

            return; // Convergence condition met
        }

        evaluations++;
    }

    throw new OptimizationException("Maximum number of function evaluations reached without convergence");
}