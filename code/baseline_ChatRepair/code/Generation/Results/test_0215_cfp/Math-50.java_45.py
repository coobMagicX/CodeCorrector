protected final double doSolve() throws TooManyEvaluationsException {
    final double maxEval = getMaxEvaluations(); // Define this method or variable as appropriate
    double eval = 0;

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);
    eval += 2; // Count evaluations used for f0 and f1

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
        if (eval >= maxEval) {
            throw new TooManyEvaluationsException(maxEval);
        }
        
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        eval++; // Count this evaluation

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
                    eval++; // Count this evaluation
                }
                break;
            default:
                throw new MathInternalError();
            }
        }
        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol || FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return chooseAppropriateSide(x1, x0, f1, inverted, atol, rtol);
        }
    }
}

private double chooseAppropriateSide(double x1, double x0, double f1, boolean inverted, double atol, double rtol) {
    // Logic to choose the correct side based on `allowed` value and other conditions
    // Same as in your original method.
}
