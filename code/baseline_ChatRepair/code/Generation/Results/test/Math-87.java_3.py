private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        double entry = getEntry(i, col);
        if (Math.abs(entry) > epsilon) {  // Use Math.abs to check against epsilon effectively
            if (row == null) {
                row = i;
            } else {
                // More than one non-zero entry in this column indicates it's not a basic column
                return null;
            }
