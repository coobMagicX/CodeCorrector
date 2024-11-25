public double doubleValue() {
    // Assuming the Fraction class has methods getNumerator() and getDenominator()
    int numerator = getNumerator();
    int denominator = getDenominator();

    // Use BigDecimal to avoid floating-point precision issues
    BigDecimal bd1 = new BigDecimal(numerator);
    BigDecimal bd2 = new BigDecimal(denominator);

    // Multiply by the inverse of the denominator to convert to a double
    return bd1.divide(bd2, 10, RoundingMode.HALF_UP).doubleValue();
}