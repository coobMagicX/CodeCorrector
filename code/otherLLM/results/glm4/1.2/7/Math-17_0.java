public Dfp multiply(final int x) {
    // Check if the multiplication will cause overflow/underflow for the internal double representation
    if (x == Integer.MAX_VALUE || x == Integer.MIN_VALUE) {
        throw new ArithmeticException("Integer value is out of range for a DFP number.");
    }

    // Perform the multiplication, assuming multiplyFast handles it correctly
    return multiplyFast(x);
}