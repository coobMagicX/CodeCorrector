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

    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();
    final double ftol = getFunctionValueAccuracy();

    while (true) {
        double x = x1 - (f1 * (x1 - x0)) / (f1 - f0);
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0 || Math.abs(fx) < ftol) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
        } else if (f0 * fx > 0) {
            f0 *= 0.5; // Method akin to Illinois, more conservative than Illinois
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }

        if (evaluations.getCount() >= maxEvaluations) { // Ensure not to exceed the max evaluations
            throw new TooManyEvaluationsException(maxEvaluations);
        }
    }
}
