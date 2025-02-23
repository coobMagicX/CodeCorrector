private Integer getPivotRow(SimplexTableau tableau, final int col) {
    // List initialization without the diamond operator
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            final double ratio = rhs / entry;
            final int cmp = Double.compare(ratio, minRatio);
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) {
                minRatio = ratio;
                // Clear the list and add the new best candidate
                minRatioPositions.clear();
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.isEmpty()) {
        return null;  // No pivot row found
    }

    if (minRatioPositions.size() == 1) {
        return minRatioPositions.get(0);  // Only one row, so it's the pivot
    }

    // Implement Bland's rule to prevent cycling when there's a tie
    int pivotRow = minRatioPositions.get(0);
    int minIndex = tableau.getBasicVariable(pivotRow);
    for (Integer row : minRatioPositions) {
        int basicVar = tableau.getBasicVariable(row);
        if (basicVar < minIndex) {
            minIndex = basicVar;
            pivotRow = row;
        }
    }

    return pivotRow;  // Return the chosen pivot row
}
