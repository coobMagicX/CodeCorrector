public int compareTo(Fraction object) {
    if (this == object) {
        return 0; // Handle equal fractions case.
    }
    
    // Calculate the greatest common divisor of both numerators.
    int gcdNumerator = MathUtils.gcd(this.numerator, object.numerator);
    // Normalize the numerators and denominators for comparison.
    double thisNumerator = (double) this.numerator / gcdNumerator;
    double objectNumerator = (double) object.numerator / gcdNumerator;
    
    // Calculate the greatest common divisor of both denominators.
    int gcdDenominator = MathUtils.gcd(this.denominator, object.denominator);
    // Normalize the numerators and denominators for comparison.
    double thisDenominator = (double) this.denominator / gcdDenominator;
    double objectDenominator = (double) object.denominator / gcdDenominator;

    // Perform the comparison using cross multiplication to avoid floating point precision issues.
    long crossProduct1 = (long)thisNumerator * object.objectDenominator;
    long crossProduct2 = (long)objectNumerator * this.objectDenominator;
    
    return Long.compare(crossProduct1, crossProduct2);
}