protected final double doSolve() {
    // Continue with the previously provided part of the `doSolve()` method...

    double previousX;  // store the last x1 value for comparison

    final int maxEval = getMaxEvaluations();  // Presuming there's a method to get max allowed evaluations
    int currentEval = 0;

    while (true) {
        previousX = x1;
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        currentEval++;
        if (currentEval > maxEval) {
            throw new TooManyEvaluationsException(maxEval);
        }

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // same method logic as before in your code

        x1 = x;
        f1 = fx;

        // Modifications to ensure meaningful progress in interval reduction
        if (FastMath.abs(x1 - previousX) <= atol || FastMath.abs(f1) <= ftol) {
            return lastAcceptableSideCheck(x1, x0, f1, inverted);
        }
        
        // including a check for minimal interval width
        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return lastAcceptableSideCheck(x1, x0, f1, inverted);
        }
    }
}

// This method decides which side of the interval to return based on method requirements
private double lastAcceptableSideCheck(double x1, double x0, double f1, boolean inverted) {
    switch (allowed) {
        case ANY_SIDE:
            return x1;
        case LEFT_SIDE:
            return inverted ? x1 : x0;
        case RIGHT_SIDE:
            return inverted ? x0 : x1;
        case BELOW_SIDE:
            return (f1 <= 0) ? x1 : x0;
        case ABOVE_SIDE:
            return (f1 >= 0) ? x1 : x0;
        default:
            throw new MathInternalError();
    }
}
