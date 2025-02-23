public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this;
    }
    int newNumerator = numerator / gcd;
    int newDenominator = denominator / gcd;

    // If the denominator is negative, flip signs on both numerator and denominator
    if (newDenominator < 0) {
        newNumerator = -newNumerator;
        newDenominator = -newDenominator;
    }

    return new Fraction(newNumerator, newDenominator);
}
