protected final double doSolve() {
    final int maxEvaluations = getMaxEvaluations(); // Make sure you define or obtain this appropriately.
    int evaluations = 0;

    // Initial solution and bounds
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);
    evaluations += 2;

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
            // Add appropriate logic to handle 'allowed' conditions here.
            return x1;
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            // Add appropriate logic to handle 'allowed' conditions here.
            return x1;
        }
    }
}
