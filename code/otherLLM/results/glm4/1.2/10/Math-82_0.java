private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double maxRatio = -Double.MAX_VALUE;
    Integer maxRatioPos = null;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        if (MathUtils.compareTo(entry, 0, epsilon) >= 0 && rhs > 0) { // Ensure right-hand side is positive for maximization
            final double ratio = rhs / entry;
            if (ratio > maxRatio) {
                maxRatio = ratio;
                maxRatioPos = i; 
            }
        }
    }
    return maxRatioPos;
}