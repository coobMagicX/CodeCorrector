public Fraction reduce() {
    // Ensure the denominator is positive and calculate gcd correctly.
    int sign = denominator < 0 ? -1 : 1;  // Using sign to adjust the direction if the denominator is negative.
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));

    // Reduce the fraction by the gcd
    int reducedNumerator = sign * numerator / gcd;  // sign is applied here to adjust the numerator as well
    int reducedDenominator = Math.abs(denominator / gcd); // always positive

    // Return the new reduced Fraction
    return new Fraction(reducedNumerator, reducedDenominator); // Assuming Fraction is constructed with numerator