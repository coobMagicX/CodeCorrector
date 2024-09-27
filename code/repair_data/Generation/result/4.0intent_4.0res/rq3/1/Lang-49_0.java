public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
    if (gcd == 1) {
        return this;
    }
    return new Fraction(numerator / gcd, denominator / gcd);
}