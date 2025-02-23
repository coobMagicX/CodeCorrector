protected final double doSolve() {
    int evaluationCount = 0;
    final int maxEvaluations = getMaxEvaluations();

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluationCount++;
    double f1 = computeObjectiveValue(x1);
    evaluationCount++;

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
        if (evaluationCount >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        double fx = computeObjectiveValue(x);
        evaluationCount++;

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // Apply the chosen method.
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
                        evaluationCount++;
                    }
                    break;
                default:
                    throw new MathInternalError();
            }
        }
        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return checkMethodConvergence(inverted, x1);
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return checkMethodConvergence(inverted, x1, x0, f1);
        }
    }
}

private double checkMethodConvergence(boolean inverted, double x1) {
    switch (allowed) {
        case ANY_SIDE:
            return x1;
        case LEFT_SIDE:
            return inverted ? x1 : x0;
        case RIGHT_SIDE:
            return inverted ? x0 : x1;
        case BELOW_SIDE:
            return f1 <= 0 ? x1 : x0;
        case ABOVE_SIDE:
            return f1 >= 0 ? x1 : x0;
        default:
            throw new MathInternalError();
    }
}

private double checkMethodConvergence(boolean inverted, double x1, double x0, double f1) {
    switch (allowed) {
        case ANY_SIDE:
            return x1;
        case LEFT_SIDE:
            return inverted ? x1 : x0;
        case RIGHT_SIDE:
            return inverted ? x0 : x1;
        case BELOW_SIDE:
            return f1 <= 0 ? x1 : x0;
        case ABOVE_SIDE:
            return f1 >= 0 ? x1 : x0;
        default:
            throw new MathInternalError();
    }
}
