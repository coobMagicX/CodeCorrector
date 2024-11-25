public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    
    // Robust comparison with a small epsilon value
    return (Math.abs(nOd - dOn) > 1e-9) ? 
           ((nOd < dOn) ? -1 : +1) : 0;
}