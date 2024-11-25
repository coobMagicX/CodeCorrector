private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;

    // Check for zero entries in the given column to handle degenerate cases or when no pivot can be found.
    boolean isColumnZero = true;
    for (int i = 0; i < tableau.getNumRows(); i++) {
        double entry = tableau.getEntry(i, col);
        if (!MathUtils.isCloseTo(entry, 0, epsilon)) {
            isColumnZero = false;
            break;
        }
    }

    // If the column is entirely zeros, return null to indicate no pivot.
    if (isColumnZero) {
        return null;
    }

    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double entry = tableau.getEntry(i, col);
        if (MathUtils.compareTo(entry, 0, epsilon) > 0) { // Check if the entry is positive
            final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
            if (rhs <= 0) {
                return i; // If right-hand side is non-positive, this row can be chosen as pivot.
            }
            final double ratio = rhs / entry;
            if (ratio < minRatio && MathUtils.compareTo(ratio, Double.MAX_VALUE, epsilon) != 0) {
                minRatio = ratio;
                minRatioPos = i; 
            }
        }
    }

    return minRatioPos;
}