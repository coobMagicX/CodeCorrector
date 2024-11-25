public Fraction reduce() {
    int numerator = Math.abs(this.numerator); // Ensuring we use 'this' to get the current fraction's numerator
    int denominator = Math.abs(this.denominator); // Ensuring we use 'this' to get the current fraction's denominator

    int gcd = greatestCommonDivisor(numerator, denominator);
    if (gcd == 1) {
        return this;
    }

    // Correctly check for zero before performing division
    if (denominator != 0) {
        return new Fraction(numerator / gcd, denominator / gcd);
    } else {
        // If the denominator is zero and gcd is 1, we still need to ensure that we're not returning a fraction with a zero denominator.
        // This is a safeguard against an uninitialized or incorrect state of the object.
        if (numerator != 0) {
            return new Fraction(numerator / gcd, 1);
        } else {
            // If both numerator and denominator are zero, we should ideally return Fraction.ZERO. However,
            // there might be a requirement to handle this case differently depending on the context.
            // Assuming that you want to return a valid fraction (e.g., 0/1) for the zero fraction, use:
            return new Fraction(0, 1);
        }
    }
}

// This method needs to be defined and implemented correctly in the class
private static int greatestCommonDivisor(int a, int b) {
    // Implementation of Euclid's algorithm for finding GCD
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return Math.abs(a); // Ensuring the GCD is non-negative
}