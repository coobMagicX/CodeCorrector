private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i;
            } else {
                // Since the new test case failed, it seems the issue might be with handling multiple non-zero entries.
                // To address this, we should continue searching for the first row with a non-zero entry
                // and ignore subsequent non-zero entries without returning null.
                continue;
            }
        }
    }
    return row;
}