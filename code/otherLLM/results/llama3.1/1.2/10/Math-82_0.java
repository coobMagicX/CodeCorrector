private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        
        // Check if entry is zero to avoid division by zero
        if (MathUtils.compareTo(entry, 0, epsilon) > 0) { 
            final double ratio = rhs / entry;
            if (ratio < minRatio) {
                minRatio = ratio;
                minRatioPos = i; 
            }
        } else if (entry == 0 && rhs != 0) {
            // Handle the case where entry is zero and rhs is not
            return i; // Return the index immediately if rhs is not zero
        }
    }
    return minRatioPos;
}