public Fraction reduce() {
    // Check if numerator is zero
    if (numerator == 0) {
        return Fraction.ZERO;
    }
    
    // Adjust the sign if denominator is negative
    int adjustedNumerator = numerator;
    int adjustedDenominator = denominator;
    if (denominator < 0) {
        adjustedNumerator = -adjustedNumerator;
        adjustedDenominator = -adjustedDenominator;
    }
    
    int gcd = Fraction.greatestCommonDivisor(Math.abs(adjustedNumerator), adjustedDenominator);
    if (gcd == 1) {
        return this;
    }
    
    // Reduce the fraction by dividing both numerator and denominator by gcd
    adjustedNumerator /= gcd;
    adjustedDenominator /= gcd;
    
    // Check if the reduced fraction is already in its simplest form
    if (adjustedDenominator == 1) {
        return Fraction.getFraction(adjustedNumerator);
    }
    
    return Fraction.getFraction(adjustedNumerator, adjustedDenominator);
}