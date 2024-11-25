public double percentageValue() {
    final long product = multiply(100).longValue();
    if (product > Integer.MAX_VALUE || product < Integer.MIN_VALUE) {
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, 100, multiply(1));
    }
    return product;
}