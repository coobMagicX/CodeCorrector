private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = 0; i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null || i != row) {
                row = i;
            } else {
                // If there are multiple rows with non-zero values at the specified column index,
                // return one of them
                return row;
            }
        }
    }
    return row;
}