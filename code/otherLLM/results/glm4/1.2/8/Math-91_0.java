public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();

    // Adding a tolerance for floating-point comparison to ensure precision
    final double TOLERANCE = 1e-9;

    if (Math.abs(nOd - dOn) < TOLERANCE) {
        return 0;
    } else if (nOd < dOn - TOLERANCE) { // Ensure we are below the value for correctness
        return -1;
    } else {
        return +1;
    }
}