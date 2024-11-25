private Integer getPivotRow(SimplexTableau tableau, final int col) {
    // create a list of all the rows that tie for the lowest score in the minimum ratio test
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0d, maxUlps) > 0) { // Ensure we're not dividing by zero
            final double ratio = rhs / entry;
            // check if the entry is strictly equal to the current min ratio
            int cmp = Double.compare(ratio, minRatio);
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) { // Update only if a new minimum ratio is found
                minRatio = ratio;
                minRatioPositions.clear(); // Clear the list and add the current row
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.isEmpty()) {
        return null; // No pivot row can be selected if no positive entries are found
    } else if (minRatioPositions.size() > 1) {
        // there's a degeneracy as indicated by a tie in the minimum ratio test

        // Check for an artificial variable that can be forced out of the basis
        for (Integer row : minRatioPositions) {
            for (int i = 0; i < tableau.getNumArtificialVariables(); i++) {
                int column = i + tableau.getArtificialVariableOffset();
                final double entry = tableau.getEntry(row, column);
                if (Precision.equals(entry, 1d, maxUlps) && row.equals(tableau.getBasicRow(column))) {
                    return row;
                }
            }
        }

        // Apply Bland's rule to prevent cycling
        Integer minRow = null;
        int minIndex = tableau.getWidth();
        for (Integer row : minRatioPositions) {
            if (tableau.getNumObjectiveFunctions() <= row && row < tableau.getWidth()) { // Ensure it is a valid row index
                int i = tableau.getNumObjectiveFunctions();
                while (i < tableau.getWidth() - 1 && minRow != row) {
                    if (row == tableau.getBasicRow(i)) {
                        if (i < minIndex) {
                            minIndex = i;
                            minRow = row;
                        }
                    }
                    i++;
                }
            }
        }
        return minRow;
    }
    // Return the first row from the list of rows with the minimum ratio
    return minRatioPositions.get(0);
}