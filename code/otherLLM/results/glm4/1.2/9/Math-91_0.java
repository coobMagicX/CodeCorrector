public int compareTo(Fraction object) {
    if (object == null) {
        throw new NullPointerException("The fraction must not be null");
    }

    double nOd = doubleValue(this);
    double dOn = object.doubleValue(object);

    // Use BigDecimal to handle precision issues with the conversion to double
    BigDecimal bdNumerator = new BigDecimal(numerator);
    BigDecimal bdDenominator = new BigDecimal(denominator);
    BigDecimal bdObjectNumerator = new BigDecimal(object.numerator);
    BigDecimal bdObjectDenominator = new BigDecimal(object.denominator);

    // Calculate the two fractions as BigDecimal values for a precise comparison
    BigDecimal fractionValue = bdNumerator.divide(bdDenominator, 50, RoundingMode.HALF_UP);
    BigDecimal objectFractionValue = bdObjectNumerator.divide(bdObjectDenominator, 50, RoundingMode.HALF_UP);

    return fractionValue.compareTo(objectFractionValue);
}

private double doubleValue(Fraction fraction) {
    // Assuming a method to handle the conversion of Fraction to double with high precision
    return (new BigDecimal(fraction.numerator).divide(new BigDecimal(fraction.denominator), 50, RoundingMode.HALF_UP)).doubleValue();
}