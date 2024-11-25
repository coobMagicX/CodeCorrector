public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();

    // To handle floating-point precision issues, use a small epsilon value to determine if two doubles are "close enough"
    final double EPSILON = 1e-9;

    // Use Math.abs for absolute difference to compare the values
    if (Math.abs(nOd - dOn) < EPSILON) {
        return 0;
    } else if (nOd < dOn) {
        return -1;
    } else {
        return +1;
    }
}