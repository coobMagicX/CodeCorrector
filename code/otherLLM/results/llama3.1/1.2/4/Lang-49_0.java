public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 0) { // Repair strategy 1: Handle fractions with zero numerator or denominator
        return null; // or a special "undefined" fraction
    }
    int newNumerator = numerator / gcd;
    int newDenominator = denominator / gcd;
    if (newDenominator == 1 && newNumerator != 0) { // Repair strategy 2: Handle edge cases where GCD is not 1
        return getFraction(newNumerator);
    }
    return getFraction(newNumerator, newDenominator); // Ensure correct results in test cases
}