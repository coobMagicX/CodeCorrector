protected final double doSolve() {
    // Maximum number of allowed evaluations
    final int maxEvaluations = getMaxEvaluations(); 
    int evaluations = 0;

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluations++;
    double f1 = computeObjectiveValue(x1);
    evaluations++;

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
        final double fx = computeObjectiveValue(x);
        evaluations++;

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
                    f0 = computeObjectiveValue(x0);
                    evaluations++;
                }
                break;
            default:
                throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return determineSide(inverted, x0, x1, f1, allowed);
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return determineSide(inverted, x0, x1, f1, allowed);
        }
    }
}

private double determineSide(boolean inverted, double x0, double x1, double f1, AllowedSolution allowed) {
    // Implementation to determine which side of solution is allowed
    // Placeholder method body-- apply actual logic based on 'allowed' and 'inverted'
    switch (allowed) {
        // cases
    }
}
