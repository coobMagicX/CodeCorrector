protected final double doSolve() throws TooManyEvaluationsException {
    final int maxEval = getMaxEvaluations(); // method to get the maximum allowed evaluations
    int evalCount = 0; // Maintain count of evaluations

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    if (++evalCount > maxEval) {
        throw new TooManyEvaluationsException(maxEval);
    }
    double f1 = computeObjectiveValue(x1);
    if (++evalCount > maxEval) {
        throw new TooManyEvaluationsException(maxEval);
    }

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
        if (++evalCount > maxEval) {
            throw new TooManyEvaluationsException(maxEval);
        }

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
                    f0 = computeObjectiveValue(x0);
                    if (++evalCount > maxEval) {
                        throw new TooManyEvaluationsException(maxEval);
                    }
                }
                break;
            default:
                throw new MathInternalError();
            }
        }
        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return handleAllowedSolution(x1, inverted);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return handleAllowedSolution(x1, inverted);
        }
    }
}
