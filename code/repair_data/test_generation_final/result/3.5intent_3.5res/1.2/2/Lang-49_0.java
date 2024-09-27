public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 0) {
        return Fraction.getFraction(0, 1);
    }
    int sign = (numerator < 0) ^ (denominator < 0) ? -1 : 1;
    return Fraction.getFraction(sign * Math.abs(numerator) / gcd, Math.abs(denominator) / gcd);
}