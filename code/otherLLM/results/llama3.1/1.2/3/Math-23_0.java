protected UnivariatePointValuePair doOptimize() {
    final boolean isMinim = getGoalType() == GoalType.MINIMIZE;
    final double lo = getMin();
    final double mid = getStartValue();
    final double hi = getMax();

    // Optional additional convergence criteria.
    final ConvergenceChecker<UnivariatePointValuePair> checker
        = getConvergenceChecker();

    double a;
    double b;
    if (lo < hi) {
        a = lo;
        b = hi;
    } else {
        a = hi;
        b = lo;
    }

    double x = mid;
    double v = x;
    double w = x;
    double d = 0;
    double e = 0;
    double fx = computeObjectiveValue(x);
    if (!isMinim) {
        fx = -fx;
    }
    double fv = fx;
    double fw = fx;

    UnivariatePointValuePair previous = null;
    UnivariatePointValuePair current
        = new UnivariatePointValuePair(x, isMinim ? fx : -fx);

    int iter = 0;
    while (true) {
        final double m = 0.5 * (a + b);
        final double tol1 = relativeThreshold * FastMath.abs(x) + absoluteThreshold;
        final double tol2 = 2 * tol1;

        // Default stopping criterion.
        final boolean stop = FastMath.abs(x - m) <= tol2 - 0.5 * (b - a);

        if (!stop) {
            // ... (rest of the code remains the same)

            if (fu <= fx) {
                if (u < x) {
                    b = x;
                } else {
                    a = x;
                }
                v = w;
                fv = fw;
                w = x;
                fw = fx;
                x = u;
                fx = fu;

                // Return the initial guess as the best point if it does not converge to a better solution
                if (iter == maxIterations) {
                    return new UnivariatePointValuePair(x, isMinim ? fx : -fx);
                }
            } else {
                // ... (rest of the code remains the same)
            }
        } else { // Default termination (Brent's criterion).
            return best(current,
                         previous,
                         isMinim);
        }
        ++iter;
    }
}