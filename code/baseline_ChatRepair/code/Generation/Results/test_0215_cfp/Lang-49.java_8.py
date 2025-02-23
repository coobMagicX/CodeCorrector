public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    
    // Reduce both numerator and denominator by their GCD
    int reducedNumerator = numerator / gcd;
    int reducedDenominator = denominator / gcd;
    
    // Ensure the denominator is always positive
    if (reducedDenominator < 0) {
        reducedNumerator = -re