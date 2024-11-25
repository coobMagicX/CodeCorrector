public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    // Add a tolerance level for floating-point comparison
    final double TOLERANCE = 1e-10;
    return (nOd < dOn - TOLERANCE) ? -1 : ((nOd > dOn + TOLERANCE) ? +1 : 0);
}