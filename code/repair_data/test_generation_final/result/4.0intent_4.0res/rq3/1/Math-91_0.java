public int compareTo(Fraction object) {
    double nOd = this.doubleValue();
    double dOn = object.doubleValue();
    return (nOd < dOn) ? -1 : ((nOd > dOn) ? 1 : 0);
}