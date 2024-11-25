public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    
    // Add a tolerance or precision check to handle cases where fractions are extremely close
    final double TOLERANCE = 1e-9; // A small value to consider two numbers equal if they differ by less than this value
    
    return (Math.abs(nOd - dOn) < TOLERANCE) ? 0 : ((nOd < dOn) ? -1 : +1);
}