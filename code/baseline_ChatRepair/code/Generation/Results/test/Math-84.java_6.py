protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    while (true) {
        incrementIterationsCounter();

        // save the original vertex
        final RealPointValuePair[] original = simplex.clone(); // Use cloned version for operations
        final RealPointValuePair best = original[0];

        // perform a reflection step
        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        if (comparator.compare(reflected, best) < 0) {

            // compute the expanded simplex
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(expanded, reflected) > 0) {
                // accept the expanded simplex
                simplex = evaluateNewSimplex(original, khi, comparator);
            } else {
                // accept the reflected simplex
                simplex = evaluateNewSimplex(original, 1.0, comparator);
            }
            return; // Exit after an expansion or reflection
        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = evaluateNewSimplex(original, gamma, comparator);
        } else {
            // Perform a shrink step if no improvement
            for (int i = 1; i < simplex.length; i++) {
                simplex[i] = shrink(simplex[i], simplex[0]);
            }
        }

        // Check for convergence
        if (checkConvergence()) {
            return;
        }
    }
}

private RealPointValuePair shrink(RealPointValuePair vertex, RealPointValuePair bestVertex) {
    // Example of a shrinking strategy towards best vertex by sigma factor
    double sigma = 0.5;
    RealPoint[] newPoint = new RealPoint[vertex.getPoint().length];
    for (int i = 0; i < vertex.getPoint().length; i++) {
        newPoint[i] = vertex.getPoint()[i] + sigma * (bestVertex.getPoint()[i] - vertex.getPoint()[i]);
    }
    return evaluatePoint(newPoint);
}

private boolean checkConvergence() {
    double tolerance = 1e-6; // Example convergence criterion
    // Implement a check based on the distance between vertices in the simplex
    for (int i = 1; i < simplex.length; i++) {
        if (distance(simplex[0], simplex[i]) > tolerance) {
            return false;
        }
    }
    return true;
}

private double distance(RealPointValuePair p1, RealPointValuePair p2) {
    double sumSquare = 0.0;
    for (int i = 0; i < p1.getPoint().length; i++) {
        sumSquare += Math.pow(p1.getPoint()[i] - p2.getPoint()[i], 2);
    }
    return Math.sqrt(sumSquare);
}
