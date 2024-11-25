public Fraction reduce() {
    if (numerator == 0) {
        denominator = 1; // Special case for zero numerator: result is 0/1.
        return this;
    }

    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this;
    }
    
    // Ensure the sign of the fraction remains correct
    int sign = numerator * denominator < 0 ? -1 : 1;
    
    return Fraction.getFraction(sign * (Math.abs(numerator) / gcd), Math.abs(denominator) / gcd);
}