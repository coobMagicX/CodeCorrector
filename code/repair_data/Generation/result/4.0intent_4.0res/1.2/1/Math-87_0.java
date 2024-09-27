private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (Math.abs(getEntry(i, col)) > epsilon) { // use absolute value comparison to handle precision issues
            if (row == null) {
                row = i;
            } else {
                // If more than one non-zero entry is found, return null to indicate multiple basic variables
                return null;
            }
        }
    }
    return row;
}