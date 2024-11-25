public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    
    // Check for exact equality before comparing the values
    if (equals(object)) {
        return 0; // Return zero when the fractions are equal
    }
    
    return (nOd < dOn) ? -1 : ((nOd > dOn) ? +1 : 0);
}