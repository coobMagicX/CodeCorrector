public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 0) { // Repair 1: Handle zero numerator or denominator
        return null; // or throw an exception, depending on the desired behavior
    }
    if ((numerator / gcd < 0 && denominator / gcd > 0) ||
            (numerator / gcd > 0 && denominator / gcd < 0)) {
        return new Fraction(-(numerator / gcd), -Math.abs(denominator / gcd));
    } else {
        return Fraction.getFraction(numerator / gcd, denominator / gcd);
    }
}