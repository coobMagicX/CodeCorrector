protected final double doSolve() throws TooManyEvaluationsException {
    // Define a maximum number of evaluations
    final int MAX_EVAL = getMaxEvaluations(); // ensure this method or relevant field is defined appropriately
    int evalCount = 0;

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0); evalCount++;
    double f1 = computeObjectiveValue(x1); evalCount++;

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
        if (evalCount >= MAX_EVAL) {
            throw new TooManyEvaluationsException(MAX_EVAL);
        }

        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x); evalCount++;

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
                    x0 = 0.5 * (x0 + x1 - Math.max(rtol * Math.abs(x1), atol));
                    f0 = computeObjectiveValue(x0); evalCount++;
                }
                break;
            default:
                throw new MathInternalError();
            }
        }
        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return resolveFinalSide(x1, inverted, f1);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return resolveFinalSide(x1, inverted, f1);
        }
    }
}
