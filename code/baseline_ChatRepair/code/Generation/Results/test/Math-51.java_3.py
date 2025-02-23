protected final double doSolve() {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);
    
    // Check if the initial guesses are roots
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1);

    // Get accuracies and define the maximum iterations
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();
    final int maxIterations = getMaxEvaluations(); // Assumes a method available to get max number of evaluations
    int iterations = 0; // Counter for the number of iterations

    //Iteration loop.
    while (iterations < maxIterations) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        // Update brackets
        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
        } else {
            if (method == Method.ILLINOIS) {
                f0 *= 0.5;
            } else if (method == Method.PEGASUS) {
                f0 *= f1 / (f1 + fx);
            }
        }

        x1 = x;
        f1 = fx;

        // Check convergence
        if (Math.abs(f1) <= ftol || Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }

        iterations++; // Increment iterations
    }

    throw new MathInternalError(); // Throw error if max iterations are exceeded without convergence
}
