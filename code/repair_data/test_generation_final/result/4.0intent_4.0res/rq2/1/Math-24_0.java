protected UnivariatePointValuePair doOptimize() {
    final boolean isMinim = getGoalType() == GoalType.MINIMIZE;
    final double lo = getMin();
    final double mid = getStartValue();
    final double hi = getMax();

    // Optional additional convergence criteria.
    final ConvergenceChecker<UnivariatePointValuePair> checker = getConvergenceChecker();

    double a = Math.min(lo, hi);
    double b = Math.max(lo, hi);
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
    UnivariatePointValuePair current = new UnivariatePointValuePair(x, fx);

    int iter = 0;
    while (true) {
        final double m = 0.5 * (a + b);
        final double tol1 = relativeThreshold * Math.abs(x) + absoluteThreshold;
        final double tol2 = 2 * tol1;

        // Default stopping criterion.
        final boolean stop = Math.abs(x - m) <= tol2 - 0.5 * (b - a);
        if (stop) {
            return best(previous, current, isMinim);
        }
        
        double u;
        double fu;
        boolean isGoldenSection = true;

        // Parabolic interpolation
        if (Math.abs(e) > tol1) {
            double r = (x - w) * (fx - fv);
            double q = (x - v) * (fx - fw);
            double p = (x - v) * q - (x - w) * r;
            q = 2 * (q - r);
            if (q > 0) {
                p = -p;
            }
            q = Math.abs(q);
            double etemp = e;
            e = d;

            if (Math.abs(p) >= Math.abs(0.5 * q * etemp) || p <= q * (a - x) || p >= q * (b - x)) {
                e = (x >= m) ? a - x : b - x;
                d = GOLDEN_SECTION * e;
            } else {
                d = p / q;
                u = x + d;
                if (u - a < tol2 || b - u < tol2) {
                    d = Math.copySign(tol1, m - x);
                }
                isGoldenSection = false;
            }
        }

        if (isGoldenSection) {
            e = (x >= m) ? a - x : b - x;
            d = GOLDEN_SECTION * e;
        }

        u = x + (Math.abs(d) >= tol1 ? d : Math.copySign(tol1, d));
        fu = computeObjectiveValue(u);
        if (!isMinim) {
            fu = -fu;
        }

        previous = current;
        current = new UnivariatePointValuePair(u, fu);

        if (checker != null && checker.converged(iter, previous, current)) {
            return current;
        }

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
        } else {
            if (u < x) {
                a = u;
            } else {
                b = u;
            }
            if (fu <= fw || w == x) {
                v = w;
                fv = fw;
                w = u;
                fw = fu;
            } else if (fu <= fv || v == x || v == w) {
                v = u;
                fv = fu;
            }
        }
        ++iter;
    }
}