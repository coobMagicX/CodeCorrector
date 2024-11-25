private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    
    // Ensuring that the current column is not the objective function column
    if (col == tableau.getNumObjectiveFunctions() - 1) {
        return null; // Shouldn't be selecting pivot in the objective row itself
    }

    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        // Ensure that the coefficient of the current column is not zero or negative
        if (entry > 0) { 
            final double ratio = rhs / entry;
            
            // Use MathUtils.compareTo to handle floating point comparisons with epsilon
            int cmp = MathUtils.compareTo(ratio, Double.MAX_VALUE, epsilon);
            
            if (cmp < 0 && (minRatioPos == null || ratio < minRatio)) {
                minRatio = ratio;
                minRatioPos = i;
            }
        }
    }

    return minRatioPos;
}