private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i; // Set the first non-zero found
            } else {
                // If a second non-zero is found, return null as per the intent
                return null;
            }
        }
    }
    // Only return row if it was not set to null by finding a second non-zero value
    return (row == null) ? null : row;
}