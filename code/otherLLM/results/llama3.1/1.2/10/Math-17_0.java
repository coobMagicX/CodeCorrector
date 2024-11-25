public Dfp multiply(final int x) {
    if (x == 0) { // multiplication by zero
        return field.newDfp(0); // zero is a valid result
    }
    
    final long product = (long) this.value * x; // integer multiplication
    
    if (product > Long.MAX_VALUE || product < Long.MIN_VALUE) {
        throw new ArithmeticException("Overflow/Underflow"); // handle overflow and underflow
    }
    
    return field.newDfp(product); // create a Dfp object with the calculated product
}