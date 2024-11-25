public double percentageValue() {
    // Multiply the fraction by 100 to get the percentage representation.
    Fraction multiplied = multiply(new Fraction(100, 1));
    
    // Convert the result to a double and return it.
    return new Fraction(multiplied.getNumerator(), multiplied.getDenominator()).doubleValue();
}