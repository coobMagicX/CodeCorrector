public Fraction reduce() {
    // Ensure the denominator is positive
    if (denominator < 0) {
        numerator = -numerator;
        denominator = -denominator;
    }

    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this