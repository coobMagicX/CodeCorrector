protected UnivariatePointValuePair doOptimize() {
    final boolean isMinim = getGoalType() == GoalType.MINIMIZE;
    final double lo = getMin();
    final double mid = getStartValue();
    final double hi = getMax();

    // Optional additional convergence criteria.
    final ConvergenceChecker<UnivariatePointValuePair> checker
        = getConvergenceChecker();

    double a, b, v, w, x, d, e, fx, fv, fw;
    if (lo < hi) {
        a = lo;
        b = hi;
    } else {
        a = hi;
        b = lo;
    }

    x = mid;
    v = x;
    w = x;
    d = 0;
    e = 0;
    fx = computeObjectiveValue(x);
    if (!isMinim) {
        fx = -fx;
    }
    fv = fx;
    fw = fx;

    UnivariatePointValuePair previous = null;
    UnivariatePointValuePair current
        = new UnivariatePointValuePair(x, isMinim ? fx : -fx);
    // Best point encountered so far (which is the initial guess).

    int iter = 0;
    while (true) {
        final double m = 0.5 * (a + b);
        final double tol1 = relativeThreshold * FastMath.abs(x) + absoluteThreshold;
        final double tol2 = 2 * tol1;

        // Default stopping criterion.
        final boolean stop = FastMath.abs(x - m) <= tol2 - 0.5 * (b - a);
        if (!stop) {
            double p = 0, q = 0, r = 0, u = 0;

            if (FastMath.abs(e) > tol1) { // Fit parabola.
                r = (x - w) * (fx - fv);
                q = (x - v) * (fx - fw);
                p = (x - v) * q - (x - w) * r;
                q = 2 * (q - r);

                if (q > 0) {
                    p = -p;
                } else {
                    q = -q;
                }

                e = d;

                if (p > q * (a - x) &&
                    p < q * (b - x) &&
                    FastMath.abs(p) < FastMath.abs(0.5 * q * r)) {
                    // Parabolic interpolation step.
                    d = p / q;
                    u = x + d;

                    // f must not be evaluated too close to a or b.
                    if (u - a < tol2 || b - u < tol2) {
                        d = FastMath.copySign(tol1, d);
                    }
                } else {
                    // Golden section step.
                    e = FastMath.copySign(GOLDEN_SECTION * (b - x), x - m);
                    d = e;
                    u = x + d;
                }
            } else {
                // Golden section step.
                e = FastMath.copySign(GOLDEN_SECTION * (b - x), x - m);
                d = e;
                u = x + d;
            }

            // Update by at least "tol1".
            if (FastMath.abs(d) < tol1) {
                if (d >= 0) {
                    u = x + tol1;
                } else {
                    u = x - tol1;
                }
            }

            previous = current;
            current = new UnivariatePointValuePair(u, isMinim ? computeObjectiveValue(u) : -computeObjectiveValue(u));

            if (checker != null) {
                if (checker.converged(iter, previous, current)) {
                    return best(current, previous, isMinim);
                }
            }

            // Update a, b, v, w and x.
            if (current.getValue() <= fx) {
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
                fx = current.getValue();
            } else {
                if (u < x) {
                    a = u;
                } else {
                    b = u;
                }
                if ((current.getValue() <= fw && !(w == x)) || (v != null && (Precision.equals(v, w) || Precision.equals(v, x)))) {
                    v = w;
                    fv = fw;
                }
                if ((current.getValue() <= fv && (v != null && (Precision.equals(v, x) || Precision.equals(v, w)))) || Precision.equals(v, x)) {
                    v = u;
                    fv = current.getValue();
                }
            }
        } else { // Default termination (Brent's criterion).
            return best(current, previous, isMinim);
        }
        ++iter;
    }
}