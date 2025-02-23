protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    while (true) {

        incrementIterationsCounter();

        // save the original vertex
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];

        // Perform a reflection step
        RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);

        if (comparator.compare(reflected, best) < 0) {
            // Reflection is better than the best point, try an expansion
            RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(expanded, reflected) < 0) {
                // Expansion is better than reflection
                simplex = replaceWorst(original, expanded);
            } else {
                // Reflection is better than expansion
                simplex = replaceWorst(original, reflected);
            }
        } else {
            // Reflection is not better, try a contraction
            RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
            if (comparator.compare(contracted, best) < 0) {
                // Contraction is better than the best
                simplex = replaceWorst(original, contracted);
            } else {
                // Contraction did not help: use shrinkage
                simplex = shrink(original);
            }
        }

        // Check for convergence and exit if converged
        if (checkConvergence()) {
            return;
        }
    }
}

/**
 * Method to replace the worst point of the simplex with a new point.
 * @param simplex The original simplex.
 * @param point The new point to include in the simplex.
 * @return The updated simplex.
 */
private RealPointValuePair[] replaceWorst(RealPointValuePair[] simplex, RealPointValuePair point) {
    // Find the worst point in the current simplex
    RealPointValuePair worst = simplex[0];
    int worstIndex = 0;
    for (int i = 1; i < simplex.length; i++) {
        if (comparator.compare(simplex[i], worst) > 0) {
            worst = simplex[i];
            worstIndex = i;
        }
    }
    // Replace the worst point with the new point
    simplex[worstIndex] = point;
    return simplex;
}

/**
 * Method to shrink the simplex.
 * @param simplex The original simplex.
 * @return The shrunk simplex.
 */
private RealPointValuePair[] shrink(RealPointValuePair[] simplex) {
    // Typically, shrink each point towards the best point
    // Implement based on specific algorithm requirements
    // This is an example and may need to be adapted
    RealPointValuePair best = simplex[0];  // Assuming best is always the first element
    for (int i = 1; i < simplex.length; i++) {
        // Compute new coordinates, e.g., halfway between current point and the best point
        double[] newPoint = new double[simplex[0].point.length];
        for (int j = 0; j < newPoint.length; j++) {
            newPoint[j] = (simplex[i].point[j] + best.point[j]) / 2;
        }
        simplex[i]