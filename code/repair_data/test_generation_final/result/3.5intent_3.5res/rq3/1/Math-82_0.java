private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    double epsilon = 1e-8; // Add epsilon value here

    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        if (Math.abs(entry) > epsilon) { // Use Math.abs() to compare with epsilon
            final double ratio = rhs / entry;
            if (ratio < minRatio) {
                minRatio = ratio;
                minRatioPos = i; 
            }
        }
    }
    return minRatioPos;
}