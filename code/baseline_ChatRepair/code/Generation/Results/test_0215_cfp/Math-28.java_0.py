private Integer getPivotRow(SimplexTableau tableau, final int col) {
    List<Integer> minRatioPositions = new ArrayList<>();
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
                minRatioPositions.clear();
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.isEmpty()) {
        return null;
    }

    if (minRatioPositions.size() == 1) {
        return minRatioPositions.get(0);
    }

    // Bland's rule implementation when there is a tie in the minimum ratio test
    int pivotRow = minRatioPositions.get(0);
    int minIndex = tableau.getBasicVariable(pivotRow);
    for (int row : minRatioPositions) {
        int basicVar = tableau.getBasicVariable(row);
        if (basicVar < minIndex) {
            minIndex = basicVar;
            pivotRow = row;
        }
    }
  
    return pivotRow;
}
