public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    int sign = (denominator < 0) ? -1 : 1;  // Ensure the fraction keeps the correct sign
    int reducedNumerator = (numerator / gcd) * sign;
    int reducedDenominator = Math.abs(denominator /