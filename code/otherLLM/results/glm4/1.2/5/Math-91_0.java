public int compareTo(Fraction object) {
    if (object == null) {
        throw new IllegalArgumentException("Fraction object cannot be null");
    }

    double nOd = this.doubleValue();
    double dOn = object.doubleValue();

    // Using BigDecimal to compare with higher precision for floating-point numbers
    return Double.compare(nOd, dOn);
}