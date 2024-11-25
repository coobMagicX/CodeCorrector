private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            // Only return the first non-zero entry found
            if (row == null) {
                row = i;
            }
        }
    }
    return row; // Ensure to return the row even if it's not null
}