public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
    if (gcd == 0) { // Handle zero denominator case
        throw new ArithmeticException("Cannot simplify fraction with zero denominator");
    }
    return Fraction.getFraction(numerator / gcd, denominator / gcd);
}