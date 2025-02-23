protected final double doSolve() {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    // If one of the bounds is the exact root, return it
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    // Verify bracketing
    verifyBracketing(x0, x1);

    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();
    final int maxEval = getMaxEvaluations();
    int evalCount = 2;  // already evaluated f0 and f1

    boolean inverted = false;

    while (evalCount <= maxEval) {
        // Compute new approximation
        final double x = x1 - (f1 * (x1 - x0)) / (f1 - f0);
        final double fx = computeObjectiveValue(x);
        evalCount++;

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
                // Detect early that the algorithm is stuck
                break;
            default:
                throw new MathInternalError();
            }
        }
        
        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return returnBasedOnAllowedSide(x1, inverted, f1);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return returnBasedOnAllowedSide(x1, inverted, f1);
        }
    }

    throw new TooManyEvaluationsException(maxEval);
}

private double returnBasedOnAllowedSide(double x1, boolean inverted, double f1) {
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
