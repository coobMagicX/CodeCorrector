private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i; // Removed the else statement
            } else { // This condition will never be met as soon as a non-zero value is found, so removed this return statement
                // Do nothing or log a warning that multiple rows contain non-zero values
            }
        }
    }
    if (row == null) {
        // Return null when no valid row is found
        return null;
    } else {
        return row; // Return the first row with a non-zero value in column col
    }
}