private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i;
            } else if (getEntry(row, col) != 0.0 && MathUtils.equals(getEntry(i, col), getEntry(row, col))) {
                return row; // Return the first row with a matching non-zero entry
            }
        }
    }
    return row;
}