public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this;
    }
    int newNumerator = numerator / gcd;
    int newDenominator = denominator / gcd;

    // Ensure denominator is positive
    if (newDenominator < 0) {
        newNumerator = -newNumerator;
        newDenominator = -newDenominator;
    }

    return Fraction.getFraction(newNumerator, newDenominator);
}