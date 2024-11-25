public Fraction reduce() {
    int numeratorSign = Math.signum(numerator);
    int denominatorSign = Math.signum(denominator);

    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this;
    }
    return Fraction.getFraction(numerator * numeratorSign / gcd, 
                                denominator * denominatorSign / gcd);
}