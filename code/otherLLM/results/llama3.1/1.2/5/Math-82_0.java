private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    for (int i = 0; i < tableau.getNumObjectiveFunctions(); i++) { // Change the loop condition back to get all rows
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        
        // Check if entry is close to zero before division
        if (MathUtils.compareTo(entry, 0, epsilon) >= 0 && Math.abs(entry) > epsilon) {
            final double ratio = rhs / entry;
            
            // Ensure minRatio is updated with a finite value
            if (ratio < minRatio && !Double.isInfinite(ratio)) {
                minRatio = ratio;
                minRatioPos = i; 
            }
        } else if (MathUtils.compareTo(entry, 0, epsilon) <= 0 && Math.abs(entry) > epsilon) { // Add a new condition for negative entries
            final double ratio = Double.POSITIVE_INFINITY; // Assign positive infinity to ratio
            
            // Update minRatio and minRatioPos accordingly
            if (!Double.isInfinite(minRatio)) {
                minRatio = ratio;
                minRatioPos = i; 
            }
        }
    }
    
    return minRatioPos;
}