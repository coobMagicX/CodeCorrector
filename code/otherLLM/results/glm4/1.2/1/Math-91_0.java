public int compareTo(Fraction object) {
    // Ensure both fractions are in their simplest form for accurate comparison
    if (!this.equals(object)) {
        // Handle the case where numerators or denominators might be 0
        double nOd = this.doubleValue();
        double dOn = object.doubleValue();

        // Use BigDecimal to handle floating-point precision issues
        java.math.BigDecimal bdNod = new java.math.BigDecimal(nOd);
        java.math.BigDecimal bdDon = new java.math.BigDecimal(dOn);

        return bdNod.compareTo(bdDon);
    }
    // If the fractions are equal, return 0
    return 0;
}