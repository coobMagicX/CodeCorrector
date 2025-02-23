private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (Math.abs(getEntry(i, col)) > epsilon) {  // use absolute value for comparison
            if (row == null) {
                row = i;
            } else {
                // Multiple potential basic variables found in the column
                return null; 
            }
        }
    }
    return row;  // return the only row with a non-zero entry, or null if none or multiple found
}
