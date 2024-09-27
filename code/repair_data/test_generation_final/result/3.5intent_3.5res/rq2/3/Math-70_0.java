public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    double x = initial;
    double y = f.value(x);
    int iterations = 0;
    while (Math.abs(y) > TOLERANCE && iterations < MAX_ITERATIONS) {
        double slope = (f.value(x + DELTA) - y) / DELTA;
        double newX = x - y / slope;
        if (newX < min || newX > max) {
            break;
        }
        x = newX;
        y = f.value(x);
        iterations++;
    }
    if (Math.abs(y) <= TOLERANCE) {
        return x;
    } else {
        throw new MaxIterationsExceededException(MAX_ITERATIONS);
    }
}