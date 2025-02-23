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
            x0 = x1; // update x0 to the previous x1
            f0 = f1;
            inverted = !inverted;
        } else {
            f0 = (method == Method.PEGASUS) ? f0 * f1 / (f1 + fx) : f0 / 2;        
        }

        x1 = x; // always update x1 to the new x
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return (allowed == AllowedSolution.ANY_SIDE || acceptableSide(inverted)) ? x1 : adjustResult(x0, x1);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return (allowed == AllowedSolution.ANY_SIDE || acceptableSide(inverted)) ? x1 : adjustResult(x0, x1);
        }

        // Ensure the loop can terminate by a sanity check for max evaluations
        if (getEvaluations() > getMaxEvaluations()) { // You must have a method to count evaluations
            throw new TooManyEvaluationsException(getMaxEvaluations());
        }
    }
}

private boolean acceptableSide(boolean inverted){
    switch (allowed) {
        case LEFT_SIDE:
            return inverted;
        case RIGHT_SIDE:
            return !inverted;
        case BELOW_SIDE:
            return f1 <= 0;
        case ABOVE_SIDE:
            return f1 > 0;
        default:
            return false;
    }
}

private double adjustResult(double x0, double x1){
    switch (allowed) {
        case LEFT_SIDE:
            return inverted ? x1 : x0;
        case RIGHT_SIDE:
            return inverted ? x0 : x1;
        case BELOW_SIDE:
            return (f1 <= 0) ? x1 : x0;
        case ABOVE_SIDE:
            return (f1 >= 0) ? x1 : x0;
        default:
            throw new MathInternalError(); // default case is preserved for unexpected conditions
    }
}
