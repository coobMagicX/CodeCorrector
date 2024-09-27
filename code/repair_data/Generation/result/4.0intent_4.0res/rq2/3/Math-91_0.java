public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    double tolerance = 1.0e-10;  // Adjusted tolerance for greater precision

    if (Math.abs(nOd - dOn) < tolerance) {
        return 0;
    } else {
        return (nOd < dOn) ? -1 : 1;
    }
}