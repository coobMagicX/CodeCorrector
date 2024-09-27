private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        if (MathUtils.compareTo(entry, 0, epsilon) > 0) { // Ensure entry is strictly greater than 0
            final double ratio = rhs / entry;
            if (ratio < minRatio && ratio >= 0) { // Additional check to ensure non-negative ratios
                minRatio = ratio;
                minRatioPos = i;
            }
        }
    }
    return minRatioPos;
}