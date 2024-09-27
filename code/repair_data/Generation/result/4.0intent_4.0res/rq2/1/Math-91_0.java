public int compareTo(Fraction object) {
    if (this.equals(object)) {
        return 0;
    } else {
        double nOd = this.doubleValue();
        double dOn = object.doubleValue();
        return (nOd < dOn) ? -1 : 1;
    }
}