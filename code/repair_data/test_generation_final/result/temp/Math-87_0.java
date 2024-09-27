private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i;
            } else {
                // Modification: Instead of returning -1 immediately for multiple rows, store all rows with non-zero entries and return null if multiple are found
                return null; // Return null if multiple rows have non-zero entries
            }
        }
    }
    return row;
}