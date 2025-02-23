protected final double doSolve() {
    final double relativeAccuracy = getRelativeAccuracy();
    final double absoluteAccuracy = getAbsoluteAccuracy();
    final double functionValueAccuracy = getFunctionValueAccuracy();

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    // Check if either boundary is the root
    if (f0 == 0.0) {
        return x0;
    } else if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1);

    // Implement checking max evaluations and a counter for evaluations.
    final int maxEvaluations = getMaxEvaluations();
    int evaluations = 0;

    while (true) {
        // Reduce if max evaluations exceeded, this should throw TooManyEvaluationsException
        if (++evaluations > maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        // Intermediate value calculation
        double x = x1 - (f1 * (x1 - x0) / (f1 - f0));
        double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        // Convergence checking based on both x and f(x)
        if (Math.abs(fx) <= functionValueAccuracy || Math.abs(x1 - x0) < Math.max(relativeAccuracy * Math.abs(x1), absoluteAccuracy)) {
            return x;
        }

        // Update low or high end
        if (Math.signum(f1) * Math.signum(fx) < 0) {
            x0 = x1;
            f0 = f1;
        } else {
            // Depending on the method, adjust f0
            f0 *= (method == Method.ILLINOIS ? 0.5 : (method == Method.PEGASUS ? f1 / (f1 + fx) : 1.0));
        }

        x1 = x;
        f1 = fx;
    }
}
