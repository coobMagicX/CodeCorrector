public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this;
    }
    int newNumerator = numerator / gcd;
    int newDenominator = denominator / gcd;
    if (newDenominator < 0) {
        newNumerator *= -1;
        newDenominator = Math.abs(newDenominator);
    }
    if (newNumerator == 0) {
        newDenominator = 1;
    }
    return Fraction.getFraction(newNumerator, newDenominator);
}