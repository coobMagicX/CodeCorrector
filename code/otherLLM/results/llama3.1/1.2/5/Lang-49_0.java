public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
    if (denominator == 0) {
        throw new ArithmeticException("Cannot simplify fraction with zero denominator");
    } else if (gcd == Math.abs(denominator)) {
        return Fraction.getFraction(1, 1);
    } else {
        return Fraction.getFraction(numerator / gcd, denominator / gcd);
    }
}