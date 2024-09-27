public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    double tolerance = 1.0e-20;

    if (Math.abs(nOd - dOn) < tolerance) {
        return 0;
    } else if (nOd < dOn) {
        return -1;
    } else {
        return 1;
    }
}