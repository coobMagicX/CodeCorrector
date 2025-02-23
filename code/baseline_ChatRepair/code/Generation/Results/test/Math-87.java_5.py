private Integer getBasicRow(final int col) {
    Integer basicRow = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        double entry = getEntry(i, col);
        if (Math.abs(entry) > epsilon) { // Check with a threshold to avoid floating point precision issues.
            if (basicRow == null) {
                basicRow = i; // Set the first encountered non-zero entry as the potential basic row.
            } else {
                // If another row with a non-zero entry in this column is found, return null.
                // This suggests that the column cannot clearly define a pivot (basic variable).
                return null;
            }
        }
    }
    return basicRow; // If loop completes with only one non-zero row found, return it as the basic row