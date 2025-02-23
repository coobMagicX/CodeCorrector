public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));

    // Correcting signs: ensure the denominator is positive
    int sign = (denominator < 0) ? -1 : 1;
        
    // Simplify both numerator and denominator by gcd and adjust sign accordingly