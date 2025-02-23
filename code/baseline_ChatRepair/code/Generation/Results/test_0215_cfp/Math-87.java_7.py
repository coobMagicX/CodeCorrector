private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (Math.abs(getEntry(i, col)) > epsilon) {  // Checking if the entry is significantly non-zero
            if (row == null) {
                row = i;  // First non-zero entry found, potential pivot row
            } else {
                return null;  // More than one non-zero entry found, can't determine a unique basic row
            }
        }
    }
    return row;  // Return the row number if exactly one non-zero entry was found, otherwise null
}
