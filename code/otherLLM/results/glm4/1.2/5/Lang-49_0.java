public Fraction reduce() {
    // Handle zero fraction case
    if (numerator == 0) {
        return this; // Return original instance as it's already in simplest form
    }

    int numeratorAbs = Math.abs(numerator);
    int denominatorAbs = Math.abs(denominator);
    int gcd = greatestCommonDivisor(numeratorAbs, denominatorAbs);

    // Ensure the correct division for simplification
    boolean negative = (numerator * denominator) < 0;
    int newNumerator = negative ? -1 : 1; // Use sign of original numerator

    if (gcd == 1) {
        return Fraction.getFraction(newNumerator * numerator, denominator); // No need to divide as gcd is 1
    } else {
        return Fraction.getFraction(newNumerator * numerator / gcd, denominator / gcd);
    }
}

private int greatestCommonDivisor(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a; // Now 'a' is the GCD
}