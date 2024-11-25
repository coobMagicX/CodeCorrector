private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    if (col < 0 || col >= tableau.getWidth()) {
        throw new IllegalArgumentException("Invalid column index");
    }
    
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        
        if (MathUtils.compareTo(entry, 0, epsilon) >= 0 && 
            MathUtils.compareTo(rhs, 0, epsilon) > 0) { // Added input validation for rhs
            final double ratio = rhs / entry;
            if (ratio < minRatio) {
                minRatio = ratio;
                minRatioPos = i; 
            }
        }
    }
    return minRatioPos;
}