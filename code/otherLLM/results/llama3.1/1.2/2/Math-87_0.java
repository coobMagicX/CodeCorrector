private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null || (row != null && row != i)) {
                row = i;
            } else {
                return null;
            }
        }
    }
    return row;
}