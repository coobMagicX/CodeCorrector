private Integer getBasicRow(final int col) {
    Integer row = null;
    int nonZeroCount = 0;
    
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            nonZeroCount++;
            if (nonZeroCount > 1) {
                // More than one non-zero entry found, cannot be a basic row
                return null;
            }
            row = i; // Assign this row as the potential basic row
        }
    }
    // Return the found basic row if only one non-zero entry was found
    return row;
}
