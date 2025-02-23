protected final double doSolve() {
    // Initial bounds and evaluations
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

    while (true) {
        if (evaluations.getCount() >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        double x = x1 - (f1 * (x1 - x0)) / (f1 - f0);
        double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
        } else {
            if (method == ILLINOIS) {
                f0 *= 0.5;
            } else if (method == PEGASUS) {
                f0 *= f1 / (f1 + fx);
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return x1;
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }
    }
}
