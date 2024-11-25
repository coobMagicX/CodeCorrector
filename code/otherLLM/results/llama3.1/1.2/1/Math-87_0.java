private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = 0; i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null || (getEntry(row, col) != getEntry(i, col))) {
                row = i;
            } else {
                // Return the first basic row encountered
                return row;
            }
        }
    }
    return row;
}