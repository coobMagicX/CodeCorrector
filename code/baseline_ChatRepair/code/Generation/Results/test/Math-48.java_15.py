protected final double doSolve() {
    // Get initial solution
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
    int maxIterations = getMaxIterations();  // Get the maximally allowed number of iterations
    int iterations = 0;

    while (iterations < maxIterations) {
        iterations++;
        
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
            case ILLINOIS:
                f0 *= 0.5;
                break;
            case PEGASUS:
                f0 *= f1 / (f1 + fx);
                break;
            case REGULA_FALSI:
                if (FastMath.abs(x1 - x) < atol) {
                    throw new ConvergenceException();  // Throw convergence exception if the progress is minimal
                }
                break;
            default:
                throw new MathInternalError();  // Handling for remaining cases
            }
        }
        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return finalizeSolution(x1, inverted);
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return finalizeSolution(x1, inverted);
        }
    }
    throw new TooManyEvaluationsException(maxIterations); // Throw if the maximum iterations is reached
}

private double finalizeSolution(double x1, boolean inverted) {
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
