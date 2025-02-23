protected final double doSolve() {
    // Initial setup is correct.
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    // Correct checks for zero values.
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
    boolean inverted = x0 > x1;

    while (true) {
        double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
        } else {
            f0 = updateFunctionEstimate(method, f0, f1, fx);
        }
        
        x1 = x;
        f1 = fx;

        if (inverted) {
            double temp = x0;
            x0 = x1;
            x1 = temp;
        }

        if (FastMath.abs(f1) <= ftol || FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return (allowed == AllowedSolution.ANY_SIDE) ? x1 : adjustForAllowedSide(x0, x1, f0, f1, inverted);
        }

        verifySequence(x0, x1);
    }
}

private double updateFunctionEstimate(SolveMethod method, double f0, double f1, double fx) {
    switch (method) {
    case ILLINOIS:
        return f0 * 0.5;
    case PEGASUS:
        return f0 * f1 / (f1 + fx);
    case REGULA_FALSI:
        return f1 * fx < 0 ? f0 : f0 * 0.8;  // This is a tweak to ensure convergence.
    default:
        throw new MathInternalError();
    }
}

private void verifySequence(double x0, double x1) {
    if (x0 > x1) {
        throw new MathInternalError("Sequence out of order: x0 must be less than x1");
    }
}

private double adjustForAllowedSide(double x0, double x1, double f0, double f1, boolean inverted) {
    switch (allowed) {
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
