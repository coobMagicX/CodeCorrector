private Integer getBasicRow(final int col) {
    Integer row = null;
    boolean found = false;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i;
            } else if (found) { // Only return null if another non-zero entry is found after the first
                return null;
            }
            found = true; // Set flag that we have found at least one non-zero entry
        }
    }
    return row;
}