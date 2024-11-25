public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator)); // use Math.abs for both numerator and denominator
    if (numerator == 0) { // handle fractions with zero numerator separately
        return new Fraction(0, 1); // return a fraction with numerator 0 and denominator 1
    }
    int sign = getSign(); // get the sign of the original fraction
    return Fraction.getFraction(sign * Math.abs(numerator / gcd), sign * Math.abs(denominator / gcd));
}

// Note: I assume there's a method `getSign()` that returns the sign of the original fraction, 
// which is -1 for negative fractions and 1 for positive fractions.