protected final double doSolve() throws TooManyEvaluationsException {
    // Get initial solution and assign the max evaluations
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);
    int maxEvaluation = getMaxEvaluations(); // Assume this getter is available
    int evaluationCount = 2; // x0 and x1 already evaluated
    
    // Bracket check and return immediate results on zero function values
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
        if (evaluationCount > maxEvaluation) {
            throw new TooManyEvaluationsException(maxEvaluation);
        }

        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluationCount++;

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            switch (method) {
            case ILLINOIS -> f0 *= 0.5;
            case PEGASUS -> f0 *= f1 / (f1 + fx);
            default -> throw new MathInternalError();
            }
        }
        
        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return checkToleranceAndReturn(x0, x1, f1, inverted, atol, rtol);
        }
        
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return checkToleranceAndReturn(x0, x1, f1, inverted, atol, rtol);
        }
    }
}

private double checkToleranceAndReturn(double x0, double x1, double f1, boolean inverted,double atol, double rtol) {
    switch (allowed) {
    case ANY_SIDE -> {
        return x1;
    }
    case LEFT_SIDE -> {
        return inverted ? x1 : x0;
    }
    case RIGHT_SIDE -> {
        return inverted ? x0 : x1;
    }
    case BELOW_SIDE -> {
        return (f1 <= 0) ? x1 : x0;
    }
    case ABOVE_SIDE -> {
        return (f1 >= 0) ? x1 : x0;
    }
    default -> throw new MathInternalError();
    }
}
