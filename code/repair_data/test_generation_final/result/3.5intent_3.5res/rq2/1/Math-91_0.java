public int compareTo(Fraction object) {
    // Get the reduced fraction for both objects
    Fraction thisFraction = getReducedFraction(numerator, denominator);
    Fraction objectFraction = getReducedFraction(object.numerator, object.denominator);

    // Compare the reduced fractions
    double nOd = thisFraction.doubleValue();
    double dOn = objectFraction.doubleValue();
    if (nOd < dOn) {
        return -1;
    } else if (nOd > dOn) {
        return 1;
    } else {
        // If the reduced fractions are equal, compare the original fractions
        if (numerator < object.numerator) {
            return -1;
        } else if (numerator > object.numerator) {
            return 1;
        } else {
            // If the original numerators are equal, compare the denominators
            if (denominator < object.denominator) {
                return -1;
            } else if (denominator > object.denominator) {
                return 1;
            } else {
                // If both fractions are equal, return 0
                return 0;
            }
        }
    }
}