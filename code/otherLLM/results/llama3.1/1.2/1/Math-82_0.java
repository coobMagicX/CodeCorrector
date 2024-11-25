private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    for (int i = 0; i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        if (MathUtils.compareTo(entry, 0, epsilon) > 0 && rhs / entry < minRatio) { // changed >= to >
            minRatio = rhs / entry;
            minRatioPos = i; 
        }
    }
    return minRatioPos;
}