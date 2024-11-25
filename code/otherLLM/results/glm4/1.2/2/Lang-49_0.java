public Fraction reduce() {
    // Handle edge cases for zero numerator or denominator.
    if (numerator == 0) {
        // If the numerator is zero, the fraction is zero regardless of the denominator.
        return this;
    }

    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator)); // Ensure both are positive
    if (gcd == 1) {
        return this; // No need to simplify if gcd is 1
    }
    
    // Correctly divide both numerator and denominator by gcd.
    return new Fraction(numerator / gcd, denominator / gcd);
}

// Helper method to calculate the greatest common divisor (GCD)
private static int greatestCommonDivisor(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return Math.abs(a); // Ensure GCD is positive
}