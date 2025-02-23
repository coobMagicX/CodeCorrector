protected final double doSolve() {
    // Initial setup remains the same
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

    double x, fx;
    boolean inverted = false;

    int iterationCount = 0;
    final int maxIterations = getMaxIterations();

    while (++iterationCount <= maxIterations) {
        x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        fx = computeObjectiveValue(x);

        if (fx == 0.0 || FastMath.abs(fx) <= ftol || FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
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
                if (FastMath.abs(x1 - x) <= atol) {
                    throw new ConvergenceException(LocalizedFormats.CONVERGENCE_FAILED, maxIterations);
                }
                // Hash comparison to avoid repeat evaluations leading to TooManyEvaluationsException
                if (Double.doubleToLongBits(x1) == Double.doubleToLongBits(x)) {
                    throw new ConvergenceException(LocalizedFormats.CONVERGENCE_FAILED, maxIterations);
                }
                break;
            default:
                throw new MathInternalError();
            }
        }
        x1 = x;
        f1 = fx;
    }
    throw new ConvergenceException(LocalizedFormats.CONVERGENCE_FAILED, maxIterations);
}
