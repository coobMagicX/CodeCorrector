public Fraction reduce() {
    // Handle the case where the denominator is zero and throw an exception if necessary
    if (denominator == 0) {
        throw new ArithmeticException("The denominator must not be zero");
    }
    
    // Normalize zero numerator to the special case of Zero fraction
    if (numerator == 0) {
        return ZERO; // normalize zero.
    }

    // Handle the edge case where the denominator is Integer.MIN_VALUE and it's even
    if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0) {
        numerator /= 2;
        denominator /= 2;
    }
    
    // Handle negative values to ensure they are treated correctly
    if (denominator < 0) {
        if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: can't negate");
        }
        numerator = -numerator;
        denominator = -denominator;
    }

    // Calculate the greatest common divisor
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));

    // If gcd is 1, the fraction is already reduced, return the current instance
    if (gcd == 1) {
        return this;
    }
    
    // Reduce the numerator and denominator by their gcd
    numerator /= gcd;
    denominator /= gcd;

    // Return the new reduced fraction as a new instance to avoid returning 'this' reference
    return new Fraction(numerator, denominator);
}