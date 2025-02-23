public Fraction reduce() {
    if (denominator == 0) {
        throw new ArithmeticException("Denominator cannot be zero");
    }
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd != 1) {
        // Creating a new Fraction object, reduced by gcd
        return new Fraction(numerator / gcd, denominator / gcd);
    } else {
        // When gcd is 1, return this instance itself as it's already in reduced form.
        return this;
    }
}
``