public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    if (nOd < dOn) {
        return -1;
    } else if (nOd > dOn) {
        return 1;
    } else {
        return 0;
    }
}