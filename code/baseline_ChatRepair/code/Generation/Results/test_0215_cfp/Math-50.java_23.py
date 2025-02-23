protected final double doSolve() {
    final int maxEvaluations = getMaxEvaluations(); // maximum number of iterations allowed
    int evaluations = 0; // track the number of evaluations

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0); evaluations++;
    double f1 = computeObjectiveValue(x1); evaluations++;

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
        final double fx = computeObjectiveValue(x); evaluations++;

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
                    f0 = computeObjectiveValue(x0); evaluations++;
                }
                break;
            default:
                throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return handleAllowedSide(x1, inverted);
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return handleAllowedSide(inverted ? x0 : x1, inverted);
        }
    }
}

private double handleAllowedSide(double x, boolean inverted) {
    switch (allowed) {
    case ANY_SIDE:
        return x;
    case LEFT_SIDE:
        return inverted ? x : x0;
    case RIGHT_SIDE:
        return inverted ? x0 : x;
    case BELOW_SIDE:
        return f1 <= 0 ? x : x0;
    case ABOVE_SIDE:
        return f1 >= 0 ? x : x0;
    default:
        throw new MathInternalError();
    }
}
