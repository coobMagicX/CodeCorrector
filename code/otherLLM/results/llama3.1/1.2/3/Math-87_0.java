private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i; // assign the first non-zero value
            } else { // consider multiple valid row indices
                return row; // return the previously found non-zero value
            }
        }
    }
    return row;
}