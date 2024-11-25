private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    if (tableau.numObjectiveFunctions() == 0) {
        return null; // or throw an exception, depending on the desired behavior
    }
    
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    for (int i = 0; i < tableau.height(); i++) { // iterate over all rows
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        if (MathUtils.compareTo(entry, 0, epsilon) >= 0) {
            final double ratio = rhs / entry;
            if (ratio < minRatio) {
                minRatio = ratio;
                minRatioPos = i; 
            }
        }
    }
    return minRatioPos;
}