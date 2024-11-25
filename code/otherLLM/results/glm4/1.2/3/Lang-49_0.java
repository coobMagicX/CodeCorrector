public Fraction reduce() {
    // Ensure the denominator is not zero before proceeding.
    if (denominator == 0) {
        throw new ArithmeticException("Denominator cannot be zero.");
    }

    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    numerator /= gcd;
    denominator /= gcd;

    // Adjust signs for negative results
    if (numerator < 0) {
        numerator = -numerator;
        denominator = -denominator;
    }
    
    return this;
}