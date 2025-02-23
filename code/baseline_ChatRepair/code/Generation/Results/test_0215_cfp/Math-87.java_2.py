private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        double entry = getEntry(i, col);
        
        // Check if the entry is exactly one and the only non-zero entry in the column
        if (MathUtils.equals(entry, 1.0, epsilon) && isOnlyNonZeroInColumn(i, col)) {
            if (row == null) {
                row = i;
            } else {
                // If already found a row, it means there's more than one pivot possibility, returning null
                return null;
            }
        } else if (!MathUtils.equals(entry, 0.0, epsilon)) {
            // If the entry is non-zero and not 1, or if there's another non-zero, directly return null
            return null;
        }
    }
