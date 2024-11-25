public Dfp multiply(final int x) {
    if (x == 0 || this == 0) { // Check for multiplication by zero
        return new Dfp(0); // Return 0 when either input is zero
    }
    
    try {
        return multiplyFast(x);
    } catch (OverflowException e) {
        if (isPositiveInfinity(this)) { // Handle overflow/underflow for PINF and NINF inputs
            return getPositiveInfinity();
        } else if (isNegativeInfinity(this)) {
            return getNegativeInfinity();
        }
        
        throw new OverflowException("Integer overflow occurred"); // Re-throw the exception with a meaningful message
    }
}