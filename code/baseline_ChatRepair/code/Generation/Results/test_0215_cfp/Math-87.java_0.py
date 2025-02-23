private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        double entry = getEntry(i, col);
        if (!MathUtils.equals(entry, 0.0, epsilon)) {
            if (MathUtils.equals(entry, 1.0, epsilon)) {
                if (row == null && isOnlyElementInColumn(col, i)) {
                    row = i;
                } else {
                    return null;  // Multiple or invalid entries, not a basic column
                }
            } else {
                return null;  // Entry is non-zero but not equal to 1
            }
        }
    }
    return row;
}

private boolean isOnlyElementInColumn(final int col, final int row) {
    for (int i = 0;