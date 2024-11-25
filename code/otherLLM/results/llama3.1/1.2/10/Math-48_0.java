protected final double computeObjectiveValue(double x) {
    // Compute f(x) = exp(x) - pi^3.0 using Taylor series approximation.
    return FastMath.exp(x) - Math.pow(FastMath.PI, 3.0);
}

protected void verifyBracketing(double x0, double x1) {
    if (x0 >= x1) {
        throw new MathInternalError("Interval [" + x0 + ", " + x1 + "] is not bracketing.");
    }
}