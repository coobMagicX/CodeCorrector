public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
    if (gcd == 1) {
        return this;
    }
    numerator /= gcd;
    denominator /= gcd; // Fixing the issue with denominator calculation
    return Fraction.getFraction(numerator, denominator); // Using existing method to create a new fraction
}