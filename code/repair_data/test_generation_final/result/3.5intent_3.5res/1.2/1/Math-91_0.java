public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    double tolerance = 1.0e-15; // Adjusted tolerance value

    if (Math.abs(nOd - dOn) < tolerance) {
        return 0;
    } else {
        return (nOd < dOn) ? -1 : 1;
    }
}