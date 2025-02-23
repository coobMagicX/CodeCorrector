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
            double adjustmentFactor = 0.5; // Default factor for Illinois.
            if (method == Method.PEGASUS) {
                adjustmentFactor = f1 / (f1 + fx);
            }
            f0 *= adjustmentFactor;
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return x1;
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }

        // Check if evaluated function values are too close and not making progress
        if (Math.abs(f1 - f0) < Double.MIN_NORMAL) {
            return (x0 + x1) / 2.0;
        }
    }
}
