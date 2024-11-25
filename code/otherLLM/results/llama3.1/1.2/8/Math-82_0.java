private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    for (int i = 0; i < tableau.getHeight(); i++) { // Changed loop bounds to start from 0
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        
        // Check for division by zero and verify the loop bounds
        if (MathUtils.compareTo(entry, 0, epsilon) >= 0 && i < tableau.getHeight()) {
            final double ratio = rhs / entry;
            
            if (ratio < minRatio) {
                minRatio = ratio;
                minRatioPos = i; 
            }
        } else if (i == tableau.getHeight() - 1) { // If loop bounds are inclusive, check the last element separately
            final double ratio = rhs / entry;
            if (MathUtils.compareTo(ratio, minRatio, epsilon) <= 0 && MathUtils.compareTo(entry, 0, epsilon) >= 0) {
                minRatio = ratio;
                minRatioPos = i; 
            }
        }
    }
    return minRatioPos;
}