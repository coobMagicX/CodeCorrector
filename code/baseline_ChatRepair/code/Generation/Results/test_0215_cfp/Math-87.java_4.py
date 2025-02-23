private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        double entry = getEntry(i, col);
        if (Math.abs(entry) > epsilon) {  // Check if the entry is significantly non-zero.
            if (row == null) {
                row = i;  // Mark this row as a candidate if no other non-zero has been found.
            } else {
                return null;  // Return null if another non-zero entry was found, implying multiple candidates.
            }
        }
    }
    return row;  // Return the found basic row, or null if no non-zero was found.
}
