private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = 0; i < getHeight(); i++) { // Start from 0 instead of getNumObjectiveFunctions()
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i;
            } else {
                return row; // Changed this line to return the second non-zero row
            }
        }
    }
    return row;
}