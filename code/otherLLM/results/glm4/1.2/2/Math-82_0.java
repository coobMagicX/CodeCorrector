private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;

    for (int i = 0; i < tableau.getNumRows(); i++) { // Ensure we are iterating over all rows
        if (tableau.getEntry(i, col) > 0 && !Double.isNaN(tableau.getEntry(i, col))) { // Check if the coefficient in column 'col' is positive and not NaN
            final double rhs = tableau.getEntry(i, tableau.getWidth() - 1); // Right-hand side value for this row
            final double entry = tableau.getEntry(i, col);
            if (MathUtils.compareTo(entry, 0, epsilon) > 0) { // Ensure that the coefficient is not zero or just above zero

                final double ratio = rhs / entry; // Calculate the ratio

                if (minRatioPos == null || ratio < minRatio) { // Check for a new minimum ratio
                    minRatio = ratio;
                    minRatioPos = i;
                }
            }
        }
    }

    return minRatioPos;
}