public double solve(double min, double max) throws MaxIterationsExceededException, 
FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        // Check for zero values at the endpoints and throw exception if necessary
        if (Math.abs(yMin) < EPSILON || Math.abs(yMax) < EPSILON) {
            // Endpoint is a root, find it directly
            if (Math.abs(yMin) < EPSILON) {
                return min;
            } else if (Math.abs(yMax) < EPSILON) {
                return max;
            }
        }
        
        // neither value is close to zero and min and max do not bracket root.
        throw new IllegalArgumentException(
                "Function values at endpoints do not have different signs." +
                "  Endpoints: [" + min + "," + max + "]" + 
                "  Values: [" + yMin + "," + yMax + "]");
    } else {
        // Solve using the BrentSolver
        ret = brentSolve(f, min, max);
    }

    return ret;
}

private double brentSolve(Function f, double x0, double x1) {
    double EPSILON = 1.0E-10; // Tolerance for convergence
    double tol;

    if (Math.abs(x1 - x0) > EPSILON) {
        tol = EPSILON * Math.abs(x1) + EPSILON;
    } else {
        tol = EPSILON;
    }

    if (f.value(x0) == 0.0) return x0; // Root is at the endpoint

    double x2, p, q, r, d, min, max, u;

    while (Math.abs(x1 - x0) > tol) {
        if (f.value(x0) == f.value(x1)) { // Handle case where endpoints have same function value
            throw new MaxIterationsExceededException("Brent's method failed to converge");
        }

        d = 2.0 * (x1 - x0) * f.value((3.0 * x1 - x0) / 4.0); // Coefficients for the parabola
        if (Math.abs(d) < EPSILON) {
            d = Math.signum(f.value(x1)) * EPSILON;
        }

        x2 = x1 - f.value(x1) / d; // Parabolic interpolation

        min = Math.min(x0, x1);
        max = Math.max(x0, x1);

        if (x2 < min || x2 > max) {
            x2 = ((x1 - x0) / 2.0) * ((3.0 * x1 - x0) / 4.0); // Bisection method
        }

        u = Math.abs(x2 - x1);
        p = (3.0 - 2.0 * q) * f.value(x1);

        if (p > 0) {
            d = Math.sqrt(p * p - 2.0 * q * r);
            if (Math.abs(d) < EPSILON) {
                d = Math.signum(q) * EPSILON;
            }

            x2 = x1 + ((q + Math.signum(q) * d) / (2.0 * p));
        } else {
            x2 = min;
        }

        x0 = x1;
        x1 = x2;
    }

    return x1; // Return the root
}