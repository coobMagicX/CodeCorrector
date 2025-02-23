protected final double doSolve() throws TooManyEvaluationsException {
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
                case REGULA_FALSI:
                    // Check the movement to avoid too small interval reduction:
                    if (Math.abs(x - x1) < atol + rtol * Math.abs(x)) {
                        throw new ConvergenceException(LocalizedFormats.CONVERGENCE_FAILED);
                    }
                    break;
                default:
                    throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return x1; // simplified return based on accuracy only for demo
        }
        
        // Check for small interval
        if (Math.abs(x1 - x0) < atol + rtol * Math.abs(x1)) {
            return x1; // might need more sophisticated side handling depending on allowed
        }
    }
}
