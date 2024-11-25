public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    
    if (nOd < 0 && dOn > 0 || nOd > 0 && dOn < 0) {
        return Double.compare(nOd, dOn);
    } else {
        return (nOd < dOn) ? -1 : ((nOd > dOn) ? +1 : 0);
    }
}