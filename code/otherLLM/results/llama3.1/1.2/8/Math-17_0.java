public Dfp multiply(final int x) {
    if (isInfinity(this)) {
        // Correctly handle infinity multiplication by returning NaN for any non-zero operand
        return isInfinity(x) ? this : new Dfp(NAN);
    } else if (isNaN(this)) {
        // Propagate NaN correctly in all multiplication scenarios
        return isNaN(x) ? new Dfp(NAN) : this;
    } else if (isZero(this)) {
        // Verify that multiplying by zero results in zero, regardless of the sign of the operand
        return isZero(x) ? this : new Dfp(0);
    } else {
        // Implement correct overflow and underflow handling to prevent integer overflows when multiplying large numbers
        if (Math.abs(x) > MAX_DFP_VALUE || Math.abs(this.value) * Math.abs(x) > MAX_DFP_VALUE) {
            throw new ArithmeticException("Overflow or underflow occurred during multiplication.");
        }
        
        // Perform the actual multiplication using the existing multiplyFast method
        return multiplyFast(x);
    }
}