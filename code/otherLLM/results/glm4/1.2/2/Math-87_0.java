private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = 0; i < getHeight(); i++) { // Change the loop starting point to 0
        double entry = getEntry(i, col); // Store the value in a variable for clarity
        if (!MathUtils.equals(entry, 0.0, epsilon)) {
            if (row == null) {
                row = i;
            } else {
                return row; // Return the current row since we found more than one non-zero entry for this column
            }
        }
    }
    return row;
}