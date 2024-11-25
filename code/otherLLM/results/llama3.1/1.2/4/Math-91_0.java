public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();

    // Compare the fractions by comparing their double values.
    if (nOd < dOn) return -1; 
    else if (nOd > dOn) return +1;
    else return 0;
}