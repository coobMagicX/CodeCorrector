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

    // check convergence and termination condition
        int iterations = getIterationsCounter();
        if (iterations >= 150) { // Corrected iteration limit
            return; // Terminate loop when iteration limit is reached
        }

        double maxDiff = 1.0e-11;
        for (int i = 0; i < original.length; i++) {
            RealPointValuePair current = original[i];
            if (comparator.compare(current, best) <= -maxDiff) {
                break;
            }
            maxDiff = Math.min(maxDiff, comparator.compare(current, best));
        }
        if (maxDiff >= 1.0e-30) { // Revisit convergence checking
            continue; // Continue loop if convergence criterion is not met
        }

    }

}

}