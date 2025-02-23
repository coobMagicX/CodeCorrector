protected final double doSolve() {
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    if (f0 == 0.0) return x0;
    if (f1 == 0.0) return x1;

    verifyBracketing(x0, x1);

    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;

    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) return x;

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else if (Math.abs(fx) < ftol || Math.abs(x - x1) <= Math.max(rtol * Math.abs(x), atol)) {
            return handleAccuracyCriteria(x, x1, x0, fx, f1, ftol, rtol, atol, inverted);
        }

        f0 *= (method == Method.PEGASUS) ? f1 / (f1 + fx) : 0.5;

        x1 = x;
        f1 = fx;
    }
}

private double handleAccuracyCriteria(double x, double x1, double x0, double fx, double f1, double ftol, double rtol, double atol, boolean inverted) {
    // Handling criteria based on the provided solutions and accuracies
    // Simplified example, implement based on specific needs
    switch (allowed) {
        case ANY_SIDE:
            return x;
        case LEFT_SIDE:
            return inverted ? x1 : x0;
        case RIGHT_SIDE:
            return inverted ? x0 : x1;
        // Continue with other cases or default error handling
        default:
            throw new MathInternalError();
    }
}
