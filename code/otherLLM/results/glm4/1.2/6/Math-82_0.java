private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;

    // Ensure the pivot column has non-negative entries except for the pivot itself
    boolean isPivotColumnNonNegative = true;
    for (int i = 0; i < tableau.getNumObjectiveFunctions(); i++) {
        final double entry = tableau.getEntry(i, col);
        if (MathUtils.compareTo(entry, 0, epsilon) < 0) {
            isPivotColumnNonNegative = false;
            break;
        }
    }

    // If the pivot column has negative entries, throw an exception or handle accordingly
    if (!isPivotColumnNonNegative) {
        throw new InvalidInputException("Pivot column must have non-negative entries.");
    }

    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        // Only consider ratios if the entry is not zero or negative
        if (MathUtils.compareTo(entry, 0, epsilon) >= 0) {
            final double ratio = rhs / entry;
            if (ratio < minRatio && ratio != Double.POSITIVE_INFINITY) { // Check for infinite values
                minRatio = ratio;
                minRatioPos = i; 
            }
        }
    }

    return minRatioPos;
}