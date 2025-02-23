protected final double doSolve() {
    // Define maximum number of iterations to prevent infinite loops
    int maxIterations = 100;  
    int iteration = 0;

    // Initial solution set-up
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1);
    
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();
    
    boolean inverted = false;

    while (iteration < maxIterations) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else if (method == Method.ILLINOIS) {
            f0 *= 0.5;
        } else if (method == Method.PEGASUS) {
            f0 *= f1 / (f1 + fx);
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return x1; // Adjust as needed based on the `allowed` condition
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return x1; // Adjust as needed based on the `allowed` condition
        }

        iteration++;
    }

    // Throw exception if maximum number of iterations is reached
    throw new org.apache.commons.math.exception.TooManyEvaluationsException("Maximal count exceeded: evaluations - " + maxIterations);
}
