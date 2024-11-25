private Integer getPivotRow(SimplexTableau tableau, final int col) {
    // create a list of all the rows that tie for the lowest score in the minimum ratio test
    List<Integer> minRatioPositions = new ArrayList<>();
    double minRatio = Double.MAX_VALUE;
    boolean hasPositiveEntry = false;

    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        // Check for a positive entry in the current column to avoid division by zero
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            hasPositiveEntry = true;
            final double ratio = rhs / entry;
            // check if the entry is strictly equal to the current min ratio
            // do not use a ulp/epsilon check
            final int cmp = Double.compare(ratio, minRatio);
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) {
                minRatio = ratio;
                minRatioPositions.clear();
                minRatioPositions.add(i);
            }
        }
    }

    // If there are no positive entries in the current column, return null
    if (!hasPositiveEntry) {
        return null;
    }

    // If there's only one minimum ratio, return it directly
    if (minRatioPositions.size() == 1) {
        return minRatioPositions.get(0);
    }

    // If there are multiple minimum ratios, apply the pivot selection rules

    // Check for an artificial variable that can be forced out of the basis
    Integer pivotRow = null;
    int pivotColumn = -1;
    for (Integer row : minRatioPositions) {
        for (int i = 0; i < tableau.getNumArtificialVariables(); i++) {
            int column = i + tableau.getArtificialVariableOffset();
            final double entry = tableau.getEntry(row, column);
            if (Precision.equals(entry, 1d, maxUlps) && row.equals(tableau.getBasicRow(column))) {
                if (pivotColumn == -1 || column < pivotColumn) {
                    pivotRow = row;
                    pivotColumn = column;
                }
            }
        }
    }

    // If no artificial variable can be forced out, apply Bland's rule
    if (pivotRow == null) {
        Integer minRow = null;
        int minIndex = tableau.getWidth();
        for (Integer row : minRatioPositions) {
            int i = tableau.getNumObjectiveFunctions();
            for (; i < tableau.getWidth() - 1 && minRow != row; i++) {
                if (row == tableau.getBasicRow(i)) {
                    if (i < minIndex) {
                        minIndex = i;
                        minRow = row;
                    }
                }
            }
        }
        pivotRow = minRow;
    }

    return pivotRow;
}