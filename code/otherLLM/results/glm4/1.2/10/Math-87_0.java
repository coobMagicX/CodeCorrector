private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i; // Initialize the first non-zero entry
            } else {
                // If we find a second non-zero entry, it means there is no unique basic variable solution.
                return null;
            }
        }
    }
    // Return the last found non-zero entry or null if none was found
    return row;
}