private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = 0; i < getHeight(); i++) { // Changed the start of the loop to 0
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) { // First non-zero entry is set as basic row
                row = i;
            } else if (row != i) { // Only return null if a second non-zero entry is found for the same column
                return null;
            }
        }
    }
    return row; // Return null or the first non-zero entry's row index
}