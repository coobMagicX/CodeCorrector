public Fraction reduce() {
    if (denominator == 0) {
        // Return a default value or throw an exception, depending on your design choice.
        // For this example, I will return Fraction.ZERO as it is mentioned in the intent.
        return Fraction.ZERO;
    }
    
    int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
    if (gcd == 1) {
        return this; // If GCD is 1, the fraction is already reduced to its simplest form.
    }

    // Check for zero after dividing numerator and denominator by their GCD
    if ((numerator / gcd) == 0 || (denominator / gcd) == 0) {
        return Fraction.ZERO;
    }
    
    return Fraction.getFraction(numerator / gcd, denominator / gcd);
}