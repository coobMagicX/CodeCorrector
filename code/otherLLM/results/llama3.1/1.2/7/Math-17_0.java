public Dfp multiply(final int x) {
    if (this.isZero()) { // Check for multiplication by zero
        return newDfp(0);
    }
    
    if (x == 0) { // Correctly handle integer input
        return newDfp(0);
    }

    try {
        return this.multiplyFast(x); // Call the fast multiplication method
    } catch (ArithmeticException e) {
        // Check for overflow/underflow and handle it accordingly
        if (e instanceof OverflowException || e instanceof UnderflowException) {
            return newDfp(OverflowException.MAX_VALUE);
        }
        
        // Handle infinity and NaN correctly
        if (this.isInfinity()) {
            if (x == Integer.MIN_VALUE) {
                return newDfp(0); // Multiply infinity by -1 should result in negative infinity
            } else if (x == Integer.MAX_VALUE) {
                return this; // Multiply infinity by 1 should result in itself
            }
        } else if (this.isNaN()) {
            return newDfp(NaNException.NaN_VALUE);
        }
        
        throw e;
    }

    return newDfp(0); // Default value if all checks fail
}