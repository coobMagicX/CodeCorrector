protected final double doSolve() throws TooManyEvaluationsException {
    // Initialize the iterations counter
    int maxEval = getMaxEvaluations(); // assumed to fetch maximum evaluations allowed
    int evalCount = 0;

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evalCount++; // Increment evaluations count
    double f1 = computeObjectiveValue(x1);
    evalCount++; // Increment evaluations count

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
        if (evalCount >= maxEval) {
            throw new TooManyEvaluationsException(maxEval);
        }

        double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        double fx = computeObjectiveValue(x);
        evalCount++; // Increment evaluations count

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
                        // Update bracket if required in Regula Falsi case
                        x0 = 0.5 * (x0 + x1 - FastMath.max(rtol * FastMath.abs(x1), atol));
                        f0 = computeObjectiveValue(x0);
                        evalCount++;
                    }
                    break;
                default:
                    throw new MathInternalError();
            }
        }
        
        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol || FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return x1; // simplified close-out criteria
        }
    }
}
