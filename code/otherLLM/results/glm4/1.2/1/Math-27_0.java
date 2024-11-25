public double percentageValue() {
    long numerator = this.numerator; // Assuming numerator and denominator are private variables of a Fraction class
    long denominator = this.denominator;

    if (denominator == 0) {
        throw new ArithmeticException("Denominator cannot be zero.");
    }

    // Use BigDecimal to avoid overflow and maintain precision
    BigDecimal value = new BigDecimal(numerator).divide(new BigDecimal(denominator), RoundingMode.HALF_UP);
    return value.multiply(BigDecimal.valueOf(100)).doubleValue();
}