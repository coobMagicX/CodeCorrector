private double solve(final UnivariateRealFunction f,
                     double x0, double y0,
                     double x1, double y1,
                     double x2, double y2)
    throws MaxIterationsExceededException, FunctionEvaluationException {
        double delta = x1 - x0;
        double oldDelta = delta;

        int i = 0;
        while (i < maximalIterationCount) {
            if (Math.abs(y2) < Math.abs(y1)) {
                x0 = x1;
                x1 = x2;
                x2 = x0;
                y0 = y1;
                y1 = y2;
                y2 = y0;
            }
            if (Math.abs(y1) <= functionValueAccuracy) {
                setResult(x1, i);
                return result;
            }
            double dx = x2 - x1;
            double tolerance =
                Math.max(relativeAccuracy * Math.abs(x1), absoluteAccuracy);
            if (Math.abs(dx) <= tolerance) {
                setResult(x1, i);
                return result;
            }
            if ((Math.abs(oldDelta) < tolerance) ||
                    (Math.abs(y0) <= Math.abs(y1))) {
                delta = 0.5 * dx;
                oldDelta = delta;
            } else {
                double r3 = y1 / y0;
                double p;
                double p1;
                if (x0 == x2) {
                    p = dx * r3;
                    p1 = 1.0 - r3;
                } else {
                    double r1 = y0 / y2;
                    double r2 = y1 / y2;
                    p = r3 * (dx * r1 * (r1 - r2) - (x1 - x0) * (r2 - 1.0));
                    p1 = (r1 - 1.0) * (r2 - 1.0) * (r3 - 1.0);
                }
                if (p == 0 || Math.abs(p) < 1) {
                    delta = dx / 2;
                    oldDelta = delta;
                } else {
                    double q = p / (1 + Math.sqrt(p));
                    delta *= q * ((3 - 2 * q) * (x1 - x0) / (4 * y1));
                    if (dx < 0) delta = -delta;
                    oldDelta = delta;
                }
            }
            x1 += delta;
            y1 = f.value(x1);
            if (y1 * y2 < 0) {
                x2 = x1;
                y2 = y1;
            } else {
                x0 = x1 - delta / 2;
                y0 = f.value(x0);
            }
            i++;
        }
        throw new MaxIterationsExceededException();
    }