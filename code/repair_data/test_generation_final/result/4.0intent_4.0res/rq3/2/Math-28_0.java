private Integer getPivotRow(SimplexTableau tableau, final int col) {
    // Create a list of all the rows that tie for the lowest score in the minimum ratio test
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        if (entry > 0) { // Change from using Precision.compareTo to direct comparison for clarity
            final double ratio = rhs / entry;
            // Check if the entry is strictly equal to the current min ratio
            final int cmp = Double.compare(ratio, minRatio);
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) {
                minRatio = ratio;
                minRatioPositions.clear(); // Use clear instead of creating a new ArrayList
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.isEmpty()) {
        return null;
    } else if (minRatioPositions.size() > 1) {
        // There's a degeneracy as indicated by a tie in the minimum ratio test

        // 1. Check if there's an artificial variable that can be forced out of the basis
        for (Integer row : minRatioPositions) {
            for (int i = 0; i < tableau.getNumArtificialVariables(); i++) {
                int column = i + tableau.getArtificialVariableOffset();
                final double entry = tableau.getEntry(row, column);
                if (Precision.equals(entry, 1d, maxUlps) && row.equals(tableau.getBasicRow(column))) {
                    return row;
                }
            }
        }

        // 2. Apply Bland's rule to prevent cycling:
        //    Take the row for which the corresponding basic variable has the smallest index
        Integer minRow = null;
        int minIndex = Integer.MAX_VALUE; // Use Integer.MAX_VALUE for the initial minimum index
        for (Integer row : minRatioPositions) {
            for (int i = 0; i < tableau.getWidth() - 1; i++) { // Avoid including the last column (rhs)
                if (tableau.getBasicRow(i) != null && tableau.getBasicRow(i).equals(row)) {
                    if (i < minIndex) {
                        minIndex = i;
                        minRow = row;
                    }
                }
            }
        }
        return minRow; // Return the row with the smallest index basic variable
    }
    return minRatioPositions.get(0);
}