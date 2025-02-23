protected final double doSolve() throws TooManyEvaluationsException {
    // Define a maximum number of iterations/evaluations.
    final int maxEvaluations = getMaxEvaluations(); 
    int evaluations = 0;

    // Get initial solution.
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluations++;
    double f1 = computeObjectiveValue(x1);
    evaluations++;

    // If one of the bounds is the exact root, return it.
    if (f0 == 0.0) {
        return x0;
    } else if (f1 == 0.0) {
        return x1;
    }

    // Verify bracketing of initial solution.
    verifyBracketing(x0, x1);

    // Get accuracies.
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    // Keep track of inverted intervals.
    boolean inverted = false;

    // Keep finding better approximations.
    while (true) {
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        // Calculate the next approximation.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++;

        // If the approximate root was found, return it.
        if (fx == 0.0) {
            return x;
        }

        // Update the bounds with the new approximation.
        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            updateBoundsBasedOnMethod(); // Example placeholder for logic based on the method like ILLINOIS, PEGASUS, etc.
        }
        
        x1 = x;
        f1 = fx;

        // Check if the root is close enough based on tolerances set.
        if (isRootCloseEnough(x, x0, x1, f1, ftol, atol, rtol, inverted)) {
            return getResultBasedOnAllowedSide(inverted, x1, x0, f1);
        }
    }
}

private boolean isRootCloseEnough(double x, double x0, double x1, double fx, double ftol, double atol, double rtol, boolean inverted) {
    // Implement logic to check if root is close enough - as an example.
    return FastMath.abs(fx) <= ftol || FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol);
}

private double getResultBasedOnAllowedSide(boolean inverted, double x1, double x0, double fx) {
    // Implement logic to get result based on allowed side or throw internally if required - as an example.
    return x1; // Simply return x1 for the sake of this placeholder.
}
