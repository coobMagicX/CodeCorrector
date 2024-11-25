public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();

    // Consider a small tolerance to account for minor differences due to rounding errors
    final double TOLERANCE = 1e-12;
    
    return (Math.abs(nOd - dOn) > TOLERANCE) ? 
           ((nOd < dOn) ? -1 : +1) : 0;
}