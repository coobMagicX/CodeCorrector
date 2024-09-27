public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    double x = initial;
    double y = f.value(x);
    if (Math.abs(y) < EPSILON) {
        return x;
    }

    double minX = min;
    double maxX = max;
    double minY = f.value(minX);
    double maxY = f.value(maxX);

    double oldX = x;
    double newX = x - (y * (maxX - minX)) / (maxY - minY);
    double newY = f.value(newX);

    while (Math.abs(newX - oldX) > EPSILON && Math.abs(newY) > EPSILON) {
        if (newY * y < 0) {
            minX = oldX;
            maxX = newX;
            minY = y;
            maxY = newY;
        } else {
            minX = newX;
            maxX = oldX;
            minY = newY;
            maxY = y;
        }

        oldX = newX;
        newX = oldX - (newY * (maxX - minX)) / (maxY - minY);
        newY = f.value(newX);
    }

    return newX;
}