public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    double tolerance = 1.0e-20;

    if (Math.abs(nOd - dOn) < tolerance) {
        return 0;
    } else if (nOd < dOn) {
        return -1;
    } else if (nOd > dOn) {
        return 1;
    } else {
        // If the absolute difference between the fractions is within the tolerance,
        // compare the numerators and denominators to determine the order

        // Compare the numerators
        long n1 = this.numerator * object.denominator;
        long n2 = object.numerator * this.denominator;
        if (n1 < n2) {
            return -1;
        } else if (n1 > n2) {
            return 1;
        }

        // Compare the denominators
        long d1 = this.denominator * object.denominator;
        long d2 = object.denominator * this.denominator;
        if (d1 < d2) {
            return -1;
        } else if (d1 > d2) {
            return 1;
        }

        // If the numerators and denominators are also equal, return 0
        return 0;
    }
}