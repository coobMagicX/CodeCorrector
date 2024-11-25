public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    
    // Account for small differences between floating-point representations
    final double MARGIN_OF_ERROR = 0.00001;
    if (Math.abs(nOd - dOn) <= MARGIN_OF_ERROR) {
        return 0; // within the margin of error, consider equal
    }
    
    return (nOd < dOn) ? -1 : +1;
}