private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = 0; i < getHeight(); i++) { // Ensure consistent row indexing by starting from 0
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i;
            } else {
                return null; // Return null instead of attempting to return an already set row value
            }
        }
    }
    return row;
}