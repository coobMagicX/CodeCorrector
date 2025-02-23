private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        double entry = getEntry(i, col);
        if (!MathUtils.equals(entry, 0.0, epsilon)) {
            if (MathUtils.equals(entry, 1.0, epsilon) && isOnlyNonZeroInColumn(i, col)) {
                if (row == null) {
                    row = i;
                } else {
                    return null; // More than one non-zero entry that equals 1, invalid basic column
                }
            } else {
                return null; // Entry is non-zero and either not equal to 1, or other nonzero entries exist in column
            }
        }
    }
    return row;
}

private boolean isOnlyNonZeroInColumn(int rowNum, int colNum) {
    for (int i = 0; i < getHeight(); i++) {
        if (i != rowNum && !MathUtils.equals(getEntry(i, colNum), 0.0, epsilon)) {
            return false; // Found other non-zero entries in the same column
        }
    }
    return true;
}
