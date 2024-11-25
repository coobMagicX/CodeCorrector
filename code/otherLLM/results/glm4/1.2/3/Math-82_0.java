private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;

    for (int i = 0; i < tableau.getNumObjectiveFunctions(); i++) { // Start from 0, as the first row is the objective function
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1); // Right-hand side of the equation
        final double entry = tableau.getEntry(i, col); // Entry in the current column

        // Only consider entries that are non-negative and not zero (since ratios cannot be computed for zero)
        if (MathUtils.compareTo(entry, 0, epsilon) > 0) {
            final double ratio = rhs / entry;

            // If a smaller positive ratio is found, update minRatio and minRatioPos
            if (ratio < minRatio && !Double.isNaN(ratio)) {
                minRatio = ratio;
                minRatioPos = i;
            }
        }
    }

    return minRatioPos; // Return the position of the pivot row if a valid ratio was found, otherwise null
}