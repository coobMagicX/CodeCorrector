protected UnivariatePointValuePair doOptimize() {
    final boolean isMinim = getGoalType() == GoalType.MINIMIZE;
    final double lo = getMin();
    final double mid = getStartValue();
    final double hi = getMax();

    // Constants for the optimization
    final double relativeThreshold = 1e-10; // Example value
    final double absoluteThreshold = 1e-10; // Example value
    final double GOLDEN_SECTION = (3 - Math.sqrt(5)) / 2;

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

        final boolean stop = Math.abs(x - m) <= tol2 - 0.5 * (b - a);
        if (!stop) {
            double p = 0;
            double q = 0;
            double r = 0;
            double u = 0;

            if (Math.abs(e) > tol1) {
                r = (x - w) * (fx - fv);
                q = (x - v) * (fx - fw);
                p = (x - v) * q - (x - w) * r;
                q = 2 * (q - r);

                if (q > 0) {
                    p = -p;
                } else {
                    q = -q;
                }

                r = e;
                e = d;

                if (p > q * (a - x) && p < q * (b - x) && Math.abs(p) < Math.abs(0.5 * q * r)) {
                    d = p / q;
                    u = x + d;
                    if (u - a < tol2 || b - u < tol2) {
                        d = x < m ? tol1 : -tol1;
                    }
                } else {
                    e = x < m ? b - x : a - x;
                    d = GOLDEN_SECTION * e;
                }
            } else {
                e = x < m ? b - x : a - x;
                d = GOLDEN_SECTION * e;
            }

            u = Math.abs(d) >= tol1 ? x + d : (d >= 0 ? x + tol1 : x - tol1);
            double fu = computeObjectiveValue(u);
            if (!isMinim) {
                fu = -fu;
            }

            previous = current;
            current = new UnivariatePointValuePair(u, isMinim ? fu : -fu);

            if (checker != null && checker.converged(iter, previous, current)) {
                return best(current, previous, isMinim);
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
        } else {
            return best(current, previous, isMinim);
        }
        ++iter;
    }
}

private UnivariatePointValuePair best(UnivariatePointValuePair current, UnivariatePointValuePair previous, boolean isMinim) {
    if (previous == null || (isMinim ? current.getValue() < previous.getValue() : current.getValue() > previous.getValue())) {
        return current;
    } else {
        return previous;
    }
}