public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this;
    }
    int sign = numerator < 0 || denominator < 0 ? -1 : 1;
    return Fraction.getFraction(sign * Math.abs(numerator) / gcd, sign * Math.abs(denominator) / gcd);
}