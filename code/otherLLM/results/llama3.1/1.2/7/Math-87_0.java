private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null || (row != null && i < row)) { // Modified condition to return the first non-zero entry
                row = i;
            }
        }
    }
    return row;
}