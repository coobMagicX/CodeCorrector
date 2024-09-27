private Integer getBasicRow(final int col) {
    Integer row = null;
    int count = 0; // count the number of non-zero entries in the column
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            count++;
            if (row == null) {
                row = i;
            } else {
                return null; // multiple non-zero entries found, return null
            }
        }
    }
    if (count == 1) {
        return row; // only one non-zero entry found, return the row
    } else {
        return null; // multiple non-zero entries found, return null
    }
}