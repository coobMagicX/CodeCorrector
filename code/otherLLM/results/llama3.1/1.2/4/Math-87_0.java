private Integer getBasicRow(final int col) {
    if (getEntry(getNumObjectiveFunctions(), col) != 0.0) {
        return getNumObjectiveFunctions();
    }
    Integer row = null;
    for (int i = getNumObjectiveFunctions() + 1; i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i;
            } else {
                return null;
            }
        }
    }
    return row;
}