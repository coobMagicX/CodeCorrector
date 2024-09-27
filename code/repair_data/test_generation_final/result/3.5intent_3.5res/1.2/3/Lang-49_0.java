public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this;
    }
    int sign = (numerator < 0) ^ (denominator < 0) ? -1 : 1;
    int reducedNumerator = sign * Math.abs(numerator) / gcd;
    int reducedDenominator = Math.abs(denominator) / gcd;
    if (reducedDenominator < 0) {
        reducedNumerator = -reducedNumerator;
        reducedDenominator = -reducedDenominator;
    }
    return Fraction.getFraction(reducedNumerator, reducedDenominator);
}