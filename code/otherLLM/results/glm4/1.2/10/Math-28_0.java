private Integer getPivotRow(SimplexTableau tableau, final int col) {
    // create a list of all the rows that tie for the lowest score in the minimum ratio test
    List<Integer> minRatioPositions = new ArrayList<>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0d, maxUlps) > 0) { // Ensure the entry is positive
            final double ratio = rhs / entry;
            final int cmp = Double.compare(ratio, minRatio);
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) {
                minRatio = ratio;
                minRatioPositions.clear(); // Clear the list to start fresh
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.isEmpty()) { // No positive entries found in the column
        return null;
    } else if (minRatioPositions.size() > 1) {
        // there's a degeneracy as indicated by a tie in the minimum ratio test

        // Apply Bland's rule to prevent cycling:
        // Take the row for which the corresponding basic variable has the smallest index
        Integer minRow = null;
        int minIndex = tableau.getWidth();
        for (Integer row : minRatioPositions) {
            if (tableau.getBasicRow(row) == null || col < tableau.getNumArtificialVariables()) { // Check if row is in basis or not an artificial variable
                continue; // Skip this row if it's not a valid pivot choice
            }
            int i = tableau.getNumObjectiveFunctions();
            for (; i < tableau.getWidth() - 1 && minRow == null; i++) {
                if (row.equals(tableau.getBasicRow(i))) { // Find the basic variable in this column
                    if (i < minIndex) {
                        minIndex = i;
                        minRow = row;
                    }
                    break; // Break after finding a basic variable in this column
                }
            }
        }
        return minRow != null ? minRow : minRatioPositions.get(0); // Return the chosen row or the first one if none found
    }
    return minRatioPositions.get(0);
}