public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // return the initial guess if it is good enough
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    // return the first endpoint if it is good enough
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);
        return result;
    }

    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, initial, yMin);
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, max, yInitial);
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, max, initial, yMin, yMax);

}

private double solve(final UnivariateRealFunction f, final double a, final double b, final double ya, final double yb) {
    double c = a;
    double fc = ya;
    double d = b - a;
    double e = d;

    double sa = 0;
    double sb = 0;

    while (true) {
        if (Math.abs(fc) < Math.abs(yb)) {
            a = b;
            b = c;
            c = a;
            ya = yb;
            yb = fc;
            fc = ya;
        }

        double tol = 2 * Double.MIN_VALUE * Math.abs(b) + functionValueAccuracy;
        double m = 0.5 * (c - b);

        if (Math.abs(m) <= tol || yb == 0) {
            setResult(b, 0);
            return result;
        }

        if (Math.abs(e) < tol || Math.abs(yb) <= Math.abs(fc)) {
            d = m;
            e = d;
        } else {
            double s = yb / fc;
            double p, q, r;

            if (a == c) {
                p = 2 * m * s;
                q = 1 - s;
            } else {
                q = fc / ya;
                r = yb / ya;
                p = s * (2 * m * q * (q - r) - (b - a) * (r - 1));
                q = (q - 1) * (r - 1) * (s - 1);
            }

            if (p > 0) {
                q = -q;
            } else {
                p = -p;
            }

            s = e;
            e = d;

            if (p >= 1.5 * m * q - Math.abs(tol * q) || p >= Math.abs(0.5 * s * q)) {
                d = m;
                e = d;
            } else {
                d = p / q;
            }
        }

        a = b;
        ya = yb;

        if (Math.abs(d) > tol) {
            b += d;
        } else if (m > 0) {
            b += tol;
        } else {
            b -= tol;
        }

        yb = f.value(b);
        fc = yb;

        if ((yb > 0 && ya < 0) || (yb < 0 && ya > 0)) {
            sa = Math.abs(sa);
            sb = Math.abs(sb);
        } else {
            sa = -Math.abs(sa);
            sb = -Math.abs(sb);
        }

        if (yb == 0) {
            setResult(b, 0);
            return result;
        }

        if (Math.abs(b - a) <= tol || Math.abs(yb) <= functionValueAccuracy) {
            setResult(b, 0);
            return result;
        }
    }
}

private void verifySequence(final double a, final double b, final double c) throws FunctionEvaluationException {
    if (a >= b || b >= c) {
        throw new FunctionEvaluationException(new Object[] { a, b, c });
    }
}

private void clearResult() {
    result = Double.NaN;
}

private void setResult(final double x, final int iterationCount) {
    result = x;
}