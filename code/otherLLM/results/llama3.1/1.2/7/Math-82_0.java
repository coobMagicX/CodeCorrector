private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    for (int i = 0; i < tableau.getNumObjectiveFunctions(); i++) { // Changed from 'tableau.getHeight()' to 'tableau.getNumObjectiveFunctions()'
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        if (MathUtils.compareTo(entry, 0, epsilon) >= 0) {
            final double ratio = rhs / entry;
            if (ratio < minRatio) {
                minRatio = ratio;
                minRatioPos = i; // Changed from 'i' to 'i' as we are considering only the rows with objective functions
            }
        }
    }
    return minRatioPos;
}