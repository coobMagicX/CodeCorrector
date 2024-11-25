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
    } else if (comparator.compare(expanded, best) < 0) { // added this condition to check for better expansion
        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = new RealPointValuePair[]{contracted};
            return;
        }
    }

    return;

}

// check convergence
if (comparator.compare(simplex[0], best) <= 0 && comparator.compare(reflected, expanded) <= 0) { // added this condition to check for convergence
    return;
}

}