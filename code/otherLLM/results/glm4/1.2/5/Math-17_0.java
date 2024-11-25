public Dfp multiply(final int x) {
    if (x == 0) {
        // If x is 0, result should be zero regardless of the internal value of the Dfp object.
        setZero();
        return this;
    }
    
    // Check for special cases like negative multiplier.
    boolean negative = x < 0;
    if (negative) {
        x = -x; // Make x positive for multiplication
    }

    long numerator;
    int exponent;

    // Assuming multiplyFast() already takes care of the special case when x is zero
    // and sets the result to 0, we only need to handle non-zero multipliers.
    if (isZero()) {
        // If Dfp is zero, it will remain zero after multiplication by any value including a negative one.
        setZero();
        return this;
    } else {
        numerator = numerator() * x; // Multiply the numerators
        exponent = exponent() + integerExponent(x); // Adjust the exponent
    }

    if (negative) {
        negative = !negative; // Flip the sign back to original state of Dfp object.
    }

    return new Dfp(numerator, exponent, negative);
}