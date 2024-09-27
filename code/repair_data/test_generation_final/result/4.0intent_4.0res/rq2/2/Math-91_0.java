public int compareTo(Fraction object) {
    if (this.equals(object)) {
        return 0;
    } else {
        double nOd = this.doubleValue();  // Ensures that the method is called on the current instance.
        double dOn = object.doubleValue();  // Ensures that the method is called on the passed object instance.
        return (nOd < dOn) ? -1 : 1;
    }
}