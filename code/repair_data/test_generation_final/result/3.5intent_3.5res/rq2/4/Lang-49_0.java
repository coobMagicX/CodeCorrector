public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 0) {
        return Fraction.getFraction(0, 1);
    }
    int reducedNumerator = numerator / gcd;
    int reducedDenominator = denominator / gcd;
    if (reducedDenominator < 0) {
        reducedNumerator *= -1;
        reducedDenominator *= -1;
    }
    if (reducedDenominator == 1) {
        return Fraction.getFraction(reducedNumerator);
    }
    return Fraction.getFraction(reducedNumerator, reducedDenominator);
}