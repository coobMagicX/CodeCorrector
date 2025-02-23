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
                if (checkConvergence()) break;  // add a convergence check here
            } else {
                // accept the expanded simplex
                simplex = simplexFromEvaluation(expanded);
                if (checkConvergence()) break;  // add a convergence check here
            }
            continue;  // continue the loop to re-evaluate
        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = simplexFromEvaluation(contracted);
            if (checkConvergence()) break;  // add a convergence check here
        } else {
            // Reduction step: shrink the simplex
            reduceSimplex();
            if (checkConvergence()) break;  // add a convergence check here
        }
    }
}

private boolean checkConvergence() {
    // Implement convergence checking logic here appropriate to your problem
    // This can be based on changes in simplex size, objective function values, etc.
    return false;
}

private RealPointValuePair[] simplexFromEvaluation(RealPointValuePair evaluated) {
    // Implement logic to create a new simplex array from an evaluated point
    return new RealPointValuePair[] {evaluated};
}

private void reduceSimplex() {
    // Implement logic to reduce the size of the current simplex
}
