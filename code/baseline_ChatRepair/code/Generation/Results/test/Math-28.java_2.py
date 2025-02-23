private Integer getPivotRow(SimplexTableau tableau, final int col) {
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0, maxUlps) > 0) {
            final double ratio = rhs / entry;
            if (Double.compare(ratio, minRatio) < 0) {
                minRatio = ratio;
                minRatioPositions.clear();
                minRatioPositions.add(i);
            } else if (Double.compare(ratio, minRatio) == 0) {
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.isEmpty()) {
        return null; // No pivot row found
    }

    // When there's a tie, we apply Bland's rule prevent cycling: choose the row with the smallest index of the entering variable
    Integer selectedRow = minRatioPositions.get(0); 
    for (Integer row : minRatioPositions) {
        if (row < selectedRow) {
            selectedRow = row;
        }
    }

    return selectedRow;
}
