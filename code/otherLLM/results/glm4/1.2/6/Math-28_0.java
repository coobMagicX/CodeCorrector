private Integer getPivotRow(SimplexTableau tableau, final int col) {
    // create a list of all the rows that tie for the lowest score in the minimum ratio test
    List<Integer> minRatioPositions = new ArrayList<>();
    double minRatio = Double.MAX_VALUE;
    boolean hasPositiveRatios = false;

    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            hasPositiveRatios = true;
            final double ratio = rhs / entry;
            final int cmp = Double.compare(ratio, minRatio);

            // Update the list of rows with the minimum ratio
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) {
                minRatio = ratio;
                minRatioPositions.clear();
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.isEmpty()) {
        // Check for positive ratios, which means the problem is infeasible
        return null;
    } else if (hasPositiveRatios && minRatioPositions.size() > 1) {
        // There's a degeneracy. Apply Bland's rule to avoid cycling.

        Integer minRow = null;
        int minIndex = tableau.getWidth();
        for (Integer row : minRatioPositions) {
            for (int i = 0; i < tableau.getNumArtificialVariables(); i++) {
                int column = i + tableau.getArtificialVariableOffset();
                final double entry = tableau.getEntry(row, column);
                if (Precision.equals(entry, 1d, maxUlps)) {
                    return row;
                }
            }
        }

        // Bland's rule: choose the row with the smallest index
        for (Integer row : minRatioPositions) {
            int i = tableau.getNumObjectiveFunctions();
            while (i < tableau.getWidth() - 1 && !row.equals(tableau.getBasicRow(i))) {
                i++;
            }
            if (i < minIndex) {
                minIndex = i;
                minRow = row;
            }
        }

        return minRow;
    } else {
        // No degeneracy, just select the first row with the minimum ratio
        return minRatioPositions.get(0);
    }
}