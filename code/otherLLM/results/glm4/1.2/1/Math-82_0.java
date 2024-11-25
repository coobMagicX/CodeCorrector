private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;

    // Check if the input column is not zero to prevent division by zero
    if (MathUtils.compareTo(tableau.getEntry(0, col), 0, epsilon) < 0) {
        return null; // Cannot find a pivot in this column as it cannot be used for any row.
    }

    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        // Check if the entry in the current column is zero or positive before computing the ratio
        if (MathUtils.compareTo(entry, 0, epsilon) < 0) {
            continue; // Skip this iteration as it's not a valid pivot candidate.
        }

        final double ratio = rhs / entry;

        // Check for division by zero in the ratio calculation
        if (Double.isNaN(ratio) || Double.isInfinite(ratio)) {
            continue; // This row cannot be used as a pivot candidate due to an invalid ratio.
        }

        if (ratio < minRatio) {
            minRatio = ratio;
            minRatioPos = i;
        }
    }

    return minRatioPos;
}