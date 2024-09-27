private Integer getBasicRow(final int col) {
    Integer row = null;
    boolean foundNonZeroEntry = false; // New variable to track if a non-zero entry is found
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (!foundNonZeroEntry) {
                row = i;
                foundNonZeroEntry = true; // Mark that a non-zero entry is found
            } else {
                return null; // Return null if more than one non-zero entry is found
            }
        }
    }
    return row;
}