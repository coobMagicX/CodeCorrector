public Fraction reduce() {
    if (denominator == 0) {
        throw new ArithmeticException("Denominator cannot be zero");
    }
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd != 1) {
        return new Fraction(numerator / gcd, denominator / gcd);
   