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
        final double tol1 = relativeThreshold * Math.abs(x) + absoluteThreshold;
        final double tol2 = 2 * tol1;

        // Default stopping criterion.
        final boolean stop = Math.abs(x - m) <= tol2 - 0.5 * (b - a);
        if (!stop) {
            double p = 0;
            double q = 0;
            double r = 0;
            double u = 0;

            if (Math.abs(e) > tol1) { // Fit parabola.
                r = (x - w) * (fx - fv);
                q = (x - v) * (fx - fw);
                p = (x - v) * q - (x - w) * r;
                q = 2 * (q - r);

                if (q > 0) {
                    p = -p;
                }
                q = Math.abs(q);

                r = e;
                e = d;

                if (Math.abs(p) < Math.abs(0.5 * q * r) && p > q * (a - x) && p < q * (b - x)) {
                    // Parabolic interpolation step.
                    d = p / q;
                    u = x + d;

                    // f must not be evaluated too close to a or b.
                    if (u - a < tol2 || b - u < tol2) {
                        d = (x < m) ? tol1 : -tol1;
                    }
                } else {
                    // Golden section step.
                    e = (x < m) ? b - x : a - x;
                    d = GOLDEN_SECTION * e;
                }
            } else {
                // Golden section step.
                e = (x < m) ? b - x : a - x;
                d = GOLDEN_SECTION * e;
            }

            // Update by at least "tol1".
            u = x + ((Math.abs(d) >= tol1) ? d : ((d >= 0) ? tol1 : -tol1));

            double fu = computeObjectiveValue(u);
            if (!isMinim) {
                fu = -fu;
            }

            // User-defined convergence checker.
            previous = current;
            current = new UnivariatePointValuePair(u, isMinim ? fu : -fu);

            if (checker != null && checker.converged(iter, previous, current)) {
                return current;
            }

            // Update a, b, v, w, and x.
            if (fu <= fx) {
                if (u < x) {
                    b = x;
                } else {
                    a = x;
                }
                v = w;
                w = x;
                fv = fw;
                fw = fx;
                x = u;
                fx = fu;
            } else {
                if (u < x) {
                    a = u;
                } else {
                    b = u;
                }
                if (fu <= fw || w == x) {
                    v = w;
                    w = u;
                    fv = fw;
                    fw = fu;
                } else if (fu <= fv || v == x || v == w) {
                    v = u;
                    fv = fu;
                }
            }
        } else { // Default termination (Brent's criterion).
            return current;
        }
        ++iter;
    }
}