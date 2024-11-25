public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();

    // To avoid floating-point precision issues when comparing double values, we can use the absolute difference 
    // and a tolerance level to determine if two doubles are effectively equal.
    final double TOLERANCE = 0.00001; // Define a suitable tolerance for comparison.
    
    // Check if both fractions have the same value within the defined tolerance
    if (Math.abs(nOd - dOn) < TOLERANCE) {
        return 0;
    }

    // Use absolute values to compare numerators, which ensures that we handle negative numbers correctly.
    double numerator1 = Math.abs(this.numerator);
    double denominator1 = this.denominator;
    double numerator2 = Math.abs(object.numerator);
    double denominator2 = object.denominator;

    // Perform a cross multiplication to avoid division by zero or overflow
    boolean isThisLess = (numerator1 * object.denominator) < (numerator2 * denominator1);

    return isThisLess ? -1 : 1;
}