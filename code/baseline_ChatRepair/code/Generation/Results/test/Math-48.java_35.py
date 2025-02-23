protected final double doSolve() throws TooManyEvaluationsException, NoBracketingException {
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
    
    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            switch (method) {
            // Implementing various methods like ILLINOIS or PEGASUS if needed
                // Update the partials of f0 based on your specific method requirement
                break;
            default:
                throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return handleAccuracy(x1, f1, inverted);
        }
        
        if (isConverged(x0, x1, rtol, atol)) {
            return handleConvergence(x1, x0, f1, inverted);
        }

        incrementEvaluationCount();
    }
}

private void incrementEvaluationCount() throws TooManyEvaluationsException {
    // This method would increment and check the number of evaluations
    // Refer to existing way of how evaluation count is handled and put limits
    // Typically, throw TooManyEvaluationsException if a maximum limit is exceeded
}

private boolean isConverged(double x0, double x1, double rtol, double atol) {
    return FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol);
}

private double handleAccuracy(double x1, double fx, boolean inverted) {
    // Handle different cases based on function accuracy
    return x1; // Simplified for demonstration
}

private double handleConvergence(double x1, double x0, double fx, boolean inverted) {
    // Handle different cases when the convergence criteria meet.
    return (inverted) ? x0 : x1; // Simplified for demonstration
}
