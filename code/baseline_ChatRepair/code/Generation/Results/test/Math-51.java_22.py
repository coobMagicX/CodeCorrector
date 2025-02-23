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

    while (true) {
        double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));  // Avoid division by zero
        double fx = computeObjectiveValue(x);

        if (fx == 0.0 || Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return chooseReturnBasedOnAllowedSide(x, x0, x1, f0, f1, fx, inverted);
        }

        if (Math.abs(fx) <= ftol) {
            return x;
        }

        if ((f1 * fx) < 0) {
            x0 = x1;  // x crosses over, so switch the interval
            f0 = f1;
            inverted = !inverted;
        } else {
            // Reduce the effective function value on the 'old' side of the interval
            f0 = updateFunctionStrategy(method, f0, f1, fx);
        }
        x1 = x;
        f1 = fx;
    }
}

private double updateFunctionStrategy(Method method, double f0, double f1, double fx) {
    switch (method) {
        case ILLINOIS:
            return f0 * 0.5;
        case PEGASUS:
            return f0 * f1 / (f1 + fx);
        default:
            throw new IllegalArgumentException("Unsupported method: " + method);
    }
}

private double chooseReturnBasedOnAllowedSide(double x, double x0, double x1,
                                              double f0, double f1, double fx,
                                              boolean inverted) {
    switch (allowed) {
        case ANY_SIDE:
            return x;
        case LEFT_SIDE:
            return inverted ? x1 : x0;
        case RIGHT_SIDE:
            return inverted ? x0 : x1;
        case BELOW_SIDE:
            return (fx <= 0) ? x : x0;
        case ABOVE_SIDE:
            return (fx >= 0) ? x : x0;
        default:
            throw new IllegalStateException("Unhandled side condition");
    }
}
