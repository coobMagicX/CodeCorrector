public double percentageValue() {
    if (Math.abs(denominator) < 1e-6) { // input validation for extreme values
        throw new ArithmeticException("Denominator is too small");
    }
    return multiply(100).doubleValue(); // rounding issue, but we're trying to avoid overflow here
}