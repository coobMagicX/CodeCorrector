protected final double doSolve() {
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

    for (int nextCheck = 0; nextCheck++ < getMaxEvaluations();) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0)); // Secant step
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0 || FastMath.abs(fx) <= ftol || FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return handleAllowedSolutions(x, fx, atol, rtol, inverted);
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // Adjust f0 based on the selected method for better convergence
            adjust(f0, f1, fx);
        }

        x1 = x;
        f1 = fx;
    }
    throw new ConvergenceException();
}

private void adjust(DoubleRef f0, double f1, double fx) {
    // Adjust f0 based on method:
    switch (method) {
        case ILLINOIS:
            f0.value *= 0.5;
            break;
        case PEGASUS:
            f0.value *= f1 / (f1 + fx);
            break;
        case REGULA_FALSI:
            // Logic for REGULA_FALSI adjustment:
            if (!isImproving(f0.value, fx)) {
                f0.value *= 0.5; // fallback adjust if not improving
            }
            break;
        default:
            throw new MathInternalError();
    }
}

private boolean isImproving(double previousF, double currentF) {
    return FastMath.abs(currentF) < FastMath.abs(previousF);  //Relatively less than previous
}

private double handleAllowedSolutions(double x, double fx, double atol, double rtol, boolean inverted) {
    // handle as in the previous code snippet
}
