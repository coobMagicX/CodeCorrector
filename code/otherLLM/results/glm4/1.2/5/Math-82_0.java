private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;

    // Iterate through the rows starting from the first constraint row to the last row in the tableau
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1); // Get the right-hand side value
        final double entry = tableau.getEntry(i, col); // Get the coefficient of the current column

        // Check if there is a non-negative entry in the current column for the row
        if (MathUtils.compareTo(entry, 0, epsilon) >= 0) {
            // If the entry is not zero and the right-hand side is not zero, calculate the ratio
            if (rhs > 0) {
                final double ratio = rhs / entry;
                // Update minRatio and minRatioPos if a new minimum ratio is found
                if (ratio < minRatio) {
                    minRatio = ratio;
                    minRatioPos = i;
                }
            } else if (entry == 0 && rhs == 0) { // Special case where both the coefficient and right-hand side are zero
                // If both entry and rhs are zero, this row cannot be a pivot candidate, so continue to the next iteration
                continue;
            } // If rhs is zero and entry is not zero, do nothing as we're looking for positive ratios only
        }
    }

    return minRatioPos; // Return the index of the pivot row or null if no suitable pivot was found
}