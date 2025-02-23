protected final double doSolve() throws TooManyEvaluationsException {
    // Set maximum number of evaluations
    final int maxEval = getMaxEvaluations();
    int evalCount = 0;

    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evalCount++;
    double f1 = computeObjectiveValue(x1);
    evalCount++;

    // If one of the bounds is the exact root, return it.
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    // Verify bracketing of initial solution.
    verifyBracketing(x0, x1);

    // Loop variables
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();
    boolean inverted = false;

    while (true) {
        if (evalCount >= maxEval) {
            throw new TooManyEvaluationsException(maxEval);
        }

        // Calculate the next approximation.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evalCount++;

        // Check for exact root.
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
                    if (x == x1) {
                        x0 = 0.5 * (x0 + x1 - FastMath.max(rtol * FastMath.abs(x1), atol));
                        f0 = computeObjectiveValue(x0);
                        evalCount++;
                    }
                    break;
                default:
                    throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol || FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
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
    }
}
