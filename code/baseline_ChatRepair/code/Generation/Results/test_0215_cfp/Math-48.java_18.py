protected final double doSolve() throws TooManyEvaluationsException, ConvergenceException {
    int maxEvaluations = getMaxEvaluations(); // assuming this method exists and provides the max allowed evaluations
    int evaluations = 0;

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0); 
    evaluations++;
    double f1 = computeObjectiveValue(x1);
    evaluations++;

    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1);

    double ftol = getFunctionValueAccuracy();
    double atol = getAbsoluteAccuracy();
    double rtol = getRelativeAccuracy();
    boolean inverted = false;

    while (true) {
        if (evaluations >= maxEvaluations) {
            throw new ConvergenceException();
        }

        double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        double fx = computeObjectiveValue(x);
        evaluations++;

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
                break;
            default:
                throw new MathInternalError();
            }
        }
        
        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return determineResult(x1, inverted, f1);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return determineResult(x1, inverted, f1);
        }
    }
}

private double determineResult(double x, boolean inverted, double fx) {
    switch (allowed) {
    case ANY_SIDE:
        return x;
    case LEFT_SIDE:
        return inverted ? x : getX0(); // Assuming getX0() provides left interval bound.
    case RIGHT_SIDE:
        return inverted ? getX0() : x; // Assuming getX0() provides left interval bound.
    case BELOW_SIDE:
        return (fx <= 0) ? x : getX0(); 
    case ABOVE_SIDE:
        return (fx >= 0) ? x : getX0();
    default:
        throw new MathInternalError();
    }
}
