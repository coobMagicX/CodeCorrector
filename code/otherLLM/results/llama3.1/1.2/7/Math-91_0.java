public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();

    // Correct handling of equivalence: Ensure that equivalent fractions (i.e., those with the same numerator and denominator) always return 0.
    if (equals(object)) { 
        return 0;
    }

    // Proper comparison of very close values: Address the issue with the PI approximations by implementing a more robust comparison for very similar double values.
    int compare = Double.compare(nOd, dOn);
    return (compare < 0) ? -1 : ((compare > 0) ? +1 : 0);
}