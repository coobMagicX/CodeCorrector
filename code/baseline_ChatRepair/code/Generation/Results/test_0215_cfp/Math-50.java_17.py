protected final double doSolve() throws TooManyEvaluationsException {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);
    int maxEvaluations = getMaxEvaluations(); // You need to ensure such a function exists to get max evaluations
    int evaluations = 2; // We have already made 2 evaluations

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
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++; // Increment the count each time objective value is computed

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
                        x0 = 0.5 * (x0 + x1 - Math.max(rtol * Math.abs(x1), atol));
                        f0 = computeObjectiveValue(x0);
                        evaluations++; // Increment as computeObjectiveValue is called again
                    }
                    break;
                default:
                    throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            switch (allowed) {
                case ANY_SIDE:
                case LEFT_SIDE:
                case RIGHT_SIDE:
                case BELOW_SIDE:
                case ABOVE_SIDE:
                    return x1;
                default:
                    throw new MathInternalError();
            }
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            switch (allowed) {
                case ANY_SIDE:
                case LEFT_SIDE:
                case RIGHT_SIDE:
                case BELOW_SIDE:
                case ABOVE_SIDE:
                    return x1;
                default:
                    throw new MathInternalError();
            }
        }
    }
}
