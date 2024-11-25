protected final double solve(double lowerBound, double upperBound, UnivariateRealFunction f, int maxIter, double rtol) {
    return RegulaFalsiSolver.solve(lowerBound, upperBound, f, maxIter, rtol);
}

protected final double getAbsoluteAccuracy() {
    // This method is not shown in the original code snippet
    // For demonstration purposes only
    return 0.0001;
}

protected final double getRelativeAccuracy() {
    return 0.01;
}