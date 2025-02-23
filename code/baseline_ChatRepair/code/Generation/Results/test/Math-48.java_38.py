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

    double lastXDiff = Double.POSITIVE_INFINITY;

    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

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
                    // Additional check to reset stubborn algorithms or improve convergence.
                    f0 = modifyObjectiveValue(f0, fx, x1 - x0, lastXDiff);
                    break;
                default:
                    throw new IllegalStateException("Unknown method: " + method);
            }
        }
        
        x1 = x;
        f1 = fx;

        double currentXDiff = Math.abs(x1 - x0);
        if (Double.compare(currentXDiff, lastXDiff) == 0) {
            // No improvement, break or handle specially
            throw new ConvergenceException("Convergence has stalled");
        }
        lastXDiff = currentXDiff;

        if (Math.abs(f1) <= ftol || currentXDiff < Math.max(rtol * Math.abs(x1), atol)) {
            // Return based on side user preferences.
            return finalizeRoot(x0, x1, inverted, f1);
        }
    }
}

private double modifyObjectiveValue(double f0, double fx, double xDiff, double lastXDiff) {
    if (xDiff >= lastXDiff * 0.95) {
        return f0 * (0.9);
    } else {
        return f0;
    }
}

private double finalizeRoot(double x0, double x1, boolean inverted, double f1) {
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
            throw new IllegalStateException("Unhandled root closer argument");
    }
}
