private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        // Adjust condition to properly handle floating point precision issues.
        double entry = getEntry(i, col);
        if (Math.abs(entry) > epsilon) { // Check against epsilon to account for floating point errors.
            if (row == null) {
                row = i;  // Set this row as the potential pivot if it's the first non-zero found.
