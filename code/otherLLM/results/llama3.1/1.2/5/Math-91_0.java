public int compareTo(Fraction object) {
    if (this.equals(object)) { // Check for exact equality first
        return 0; // If equal, consider them the same
    }

    double nOd = this.doubleValue();
    double dOn = object.doubleValue();

    // Calculate the absolute difference between the two values
    double diff = Math.abs(nOd - dOn);

    // Use a small epsilon value to compare the differences
    final double EPSILON = 1e-9; // Adjust this value as needed

    if (diff < EPSILON) {
        return 0; // If close enough, consider them equal
    } else if (nOd < dOn) {
        return -1;
    } else {
        return +1;
    }
}