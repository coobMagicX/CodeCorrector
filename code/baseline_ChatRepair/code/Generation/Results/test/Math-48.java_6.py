protected final double doSolve() {
    // Get initial solution
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
            // Adjustments depending on the method being used
            switch (method) {
            case ILLINOIS:
                f0 *= 0.5;
                break;
            case PEGASUS:
                f0 *= f1 / (f1 + fx);
                break;
            case REGULA_FALSI:
                if (f0 == f1) {
                    throw new ConvergenceException();  // Checked if f0 and f1 haven't changed, which triggers a convergence warning.
                }
                break;
            default:
                throw new MathInternalError();
            }
        }
        
        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return x1; // Aproximation is considered good enough if f1 is smaller than or equal to the function tolerance
        }

        if (FastMath.abs(x1 - x0) <= FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return x1; // If the difference between x1 and x0 is lesser than max of relative tolerance times the absolute x1 and absolute tolerance, return x1
        }

        // Include a check to ensure there are not too many evaluations
        if (getEvaluations() > getMaxEvaluations()) {
            throw new TooManyEvaluationsException(getMaxEvaluations());
        }
    }
}
