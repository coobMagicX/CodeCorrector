public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
    
    // If GCD is 1, it means the fraction cannot be reduced further.
    if (gcd == 1) {
        // Return an error or a specific value instead of the original fraction.
        return new Fraction(0, 1); // Assuming this is the desired behavior
    }
    
    int sign = (numerator < 0 && denominator > 0) || (numerator > 0 && denominator < 0) ? -1 : 1;
    
    // Correctly handle sign changes by multiplying the result with the correct sign.
    return Fraction.getFraction(Math.abs(numerator) / gcd * sign, Math.abs(denominator) / gcd);
}