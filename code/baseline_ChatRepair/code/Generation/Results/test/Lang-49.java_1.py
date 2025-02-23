public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    // Apply the sign of the denominator to the numerator
    int sign = (denominator < 0) ? -1 : 1;
    return new Fraction((numerator / gcd) * sign, Math.abs(denominator / gcd));
}
