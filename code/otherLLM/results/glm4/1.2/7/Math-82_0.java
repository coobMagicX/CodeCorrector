private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;

    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double entry = tableau.getEntry(i, col);
        
        // Skip the column if it has a non-positive coefficient
        if (MathUtils.compareTo(entry, 0, epsilon) <= 0) {
            continue;
        }
        
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        if (rhs > 0) { // Only consider positive right-hand-side values
            final double ratio = rhs / entry;
            
            // Update the pivot row if a smaller ratio is found
            if (minRatioPos == null || ratio < minRatio) {
                minRatio = ratio;
                minRatioPos = i;
            }
        }
    }

    // If no positive ratios are found, it might indicate an infeasible problem
    if (minRatioPos == null) {
        throw new IllegalArgumentException("No pivot row could be found. The problem might be infeasible.");
    }

    return minRatioPos;
}