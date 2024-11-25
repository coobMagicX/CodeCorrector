private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;

    // Iterate over the rows starting from the first constraint row to the last non-objective function row.
    for (int i = 0; i < tableau.getNumConstraints(); i++) { // Use getNumConstraints() instead of getting height manually
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1); // Get the right-hand side value
        final double entry = tableau.getEntry(i, col); // Get the coefficient for the entering variable

        // Check if the current row's coefficient is non-negative and that this column corresponds to an entering variable.
        if (entry > 0 && MathUtils.compareTo(entry, 0, epsilon) >= 0) {
            final double ratio = rhs / entry; // Calculate the ratio
            // Update minRatio and minRatioPos if a smaller positive ratio is found
            if (ratio < minRatio) {
                minRatio = ratio;
                minRatioPos = i;
            }
        }
    }

    return minRatioPos; // Return the row index with the minimum positive ratio, or null if no such row exists
}