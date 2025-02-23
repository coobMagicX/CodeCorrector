protected final double doSolve() {
    // Initial solutions and evaluations
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

    double ftol = getFunctionValueAccuracy();
    double atol = getAbsoluteAccuracy();
    double rtol = getRelativeAccuracy();

    boolean inverted = false;

    while (true) {
        double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        double fx = computeObjectiveValue(x);

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
                default:
                    break;
            }
        }

        x1 = x;
        f1 = fx;

        // Addition: Ensure no minimal progress situation that would cause endless loop
        if (Math.abs(x1 - x0) < atol || Math.abs(f1) < ftol) {
            return handleNoProgressSituation(x0, x1, f0, f1, inverted);
        }
    }
}

private double handleNoProgressSituation(double x0, double x1, double f0, double f1, boolean inverted) {
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
            throw new IllegalStateException("Unexpected value situation");
    }
}
