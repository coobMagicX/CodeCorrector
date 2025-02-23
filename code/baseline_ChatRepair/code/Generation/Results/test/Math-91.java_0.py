public int compareTo(Fraction object) {
    // Compute cross products for comparison to avoid floating point inaccuracies
    long lhs = this.numerator * (long) object.denominator;
    long rhs = object.numerator * (long) this.denominator;

    if (lhs < rhs) {
        return -1;
    } else if (lhs > rhs) {
        return 1;
    } else {
        return 0;
    }
}
