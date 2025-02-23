protected final double doSolve() {
    final int MAX_EVALUATIONS = 100; // Example limit, adjust as needed
    int evalCount = 0;

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0); evalCount++;
    double f1 = computeObjectiveValue(x1); evalCount++;

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
        if (evalCount >= MAX_EVALUATIONS) {
            throw new TooManyEvaluationsException(MAX_EVALUATIONS);
        }

        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x); evalCount++;

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
                break;
            default:
                throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return resolveByAllowedSide(x1, inverted, f1);
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1),
                                                 atol)) {
            return resolveByAllowedSide(inverted ? x0 : x1, inverted, f1);
        }
    }
}

private double resolveByAllowedSide(double approx, boolean inverted, double functionValue) {
    switch (allowed) {
    case ANY_SIDE:
        return approx;
    case LEFT_SIDE:
        return inverted ? approx : (functionValue <= 0 ? approx : Double.NaN);
    case RIGHT_SIDE:
        return inverted ? (functionValue >= 0 ? approx : Double.NaN) : approx;
    case BELOW_SIDE:
        return (functionValue <= 0) ? approx : Double.NaN;
    case ABOVE_SIDE:
        return (functionValue >= 0) ? approx : Double.NaN;
    default:
        throw new MathInternalError();
    }
}
