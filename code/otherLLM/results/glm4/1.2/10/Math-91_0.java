public int compareTo(Fraction object) {
    final double EPSILON = 1e-9; // Define an appropriate epsilon for comparison

    double nOd = this.doubleValue();
    double dOn = object.doubleValue();

    if (Math.abs(nOd - dOn) <= EPSILON) {
        return 0;
    } else if (nOd < dOn) {
        return -1;
    } else {
        return 1;
    }
}