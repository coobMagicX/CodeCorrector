public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1 || numerator == 0) {
        return this;
    }
    return Fraction.getFraction(numerator / gcd, denominator / gcd);
}