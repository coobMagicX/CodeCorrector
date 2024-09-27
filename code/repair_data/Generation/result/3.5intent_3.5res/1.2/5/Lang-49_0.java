public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this;
    }
    int reducedNumerator = numerator / gcd;
    int reducedDenominator = denominator / gcd;
    if (reducedDenominator < 0) {
        reducedNumerator = -reducedNumerator;
        reducedDenominator = -reducedDenominator;
    } else if (reducedNumerator < 0) {
        reducedNumerator = -reducedNumerator;
    }
    return Fraction.getFraction(reducedNumerator, reducedDenominator);
}