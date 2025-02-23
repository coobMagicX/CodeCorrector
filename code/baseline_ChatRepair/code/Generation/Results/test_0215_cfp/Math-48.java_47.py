protected final double doSolve() {
    final int maxEvaluations = getMaxEvaluations(); // Assuming jmethod to get max allowed evaluations
    int evaluations = 0; // To track number of evaluations

    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluations++;
    double f1 = computeObjectiveValue(x1);
    evaluations++;

    // If one of the bounds is the exact root, return it.
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    // Verify bracketing of initial solution.
    verifyBracketing(x0, x1);

    // Initialization for accuracies
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();
    
    boolean inverted = false;

    while (true) {
        // Check if the number of evaluations exceeds the maximum allowed
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
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
                // Implement all methods correctly as needed
                break;
            default:
                throw new MathInternalError();
            }
        }
        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            // Based on allowed side, etc., return appropriate value
            return returnBasedOnAllowedSide(x1, x0, f1, inverted); // Implement this method to handle different allowed sides
        }
        
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return returnBasedOnAllowedSide(x1, x0, f1, inverted); // Implement this method to handle different allowed sides
        }
    }
}

private double returnBasedOnAllowedSide(double x1, double x0, double f1, boolean inverted) {
    // This method will handle different side requirements
    // Implementation of returning values based on the side specifics
    // As needed based on the 'allowed' flag. Similar to existing implementation.
    // The detailed examples in the original method body should be transferred here.
}
