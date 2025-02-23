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
            switch (method) {
                case ILLINOIS:
                    f0 *= 0.5;
                    break;
                case PEGASUS:
                    f0 *= f1 / (f1 + fx);
                    break;
                default:
                    throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        // Check if our f1 is sufficiently close to zero then it meets the condition.
        if (FastMath.abs(f1) <= ftol) {
            return x1;
        }

        // Check the new root accuracy and if it's good enough, return x1
        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return x1;
        }

        // Ensure that we are not running into too many evaluations.
        incrementEvaluationCount();
        if (getEvaluations() > getMaxEvaluations()) {
            throw new TooManyEvaluationsException(getMaxEvaluations());
        }
    }
}

private void incrementEvaluationCount() {
    // Assuming there's a field or a method that handles counting evaluations
    evaluations++;
}

private int getEvaluations() {
    // Assuming there's a way to get the current evaluation count
    return evaluations;
}

private int getMaxEvaluations() {
    // Should return the maximum allowed evaluations.
    return maxEvaluations;
}
