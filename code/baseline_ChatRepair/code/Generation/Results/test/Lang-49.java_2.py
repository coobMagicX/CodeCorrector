public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    // Check if reduction is necessary
    if (gcd == 1) {
        return this; // Return the same object if already in reduced form
    }
    // Adjust sign according to the denominator
    int sign = (denominator < 0) ? -1 : 1;
