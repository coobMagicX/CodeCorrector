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
            double updateFactor;
            switch (method) {
            case ILLINOIS:
                updateFactor = 0.5;
                break;
            case PEGASUS:
                updateFactor = f1 / (f1 + fx);
                break;
            default:
                // The method should be provided or handled accurately.
                throw new IllegalStateException("Unhandled method type");
            }
            f0 *= updateFactor;
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return selectResultBasedOnAllowedSide(x0, x1, f1, inverted);
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return selectResultBasedOnAllowedSide(x0, x1, f1, inverted);
        }
        
        checkEvaluationCount();
    }
}

protected void checkEvaluationCount() {
    incrementEvaluationCount();
    if (getEvaluations() > getMaxEvaluations()) {
        throw new TooManyEvaluationsException(getMaxEvaluations());
    }
}
