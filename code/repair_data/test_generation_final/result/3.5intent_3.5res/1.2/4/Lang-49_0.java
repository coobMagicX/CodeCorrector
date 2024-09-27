public Fraction reduce() {
    if (numerator == 0) {
        return Fraction.getFraction(0, 1);
    }
    
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this;
    }
    
    int reducedNumerator = numerator / gcd;
    int reducedDenominator = denominator / gcd;
    
    if (reducedDenominator < 0) {
        reducedNumerator *= -1;
        reducedDenominator *= -1;
    }
    
    return Fraction.getFraction(reducedNumerator, reducedDenominator);
}

private int greatestCommonDivisor(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}