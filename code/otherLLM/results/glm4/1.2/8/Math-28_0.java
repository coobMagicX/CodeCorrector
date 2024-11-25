private Integer getPivotRow(SimplexTableau tableau, final int col) {
    // create a list of all the rows that tie for the lowest score in the minimum ratio test
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0d, maxUlps) > 0) { // Check for positive values
            final double ratio = rhs / entry;
            // check if the entry is strictly equal to the current min ratio
            final int cmp = Double.compare(ratio, minRatio);
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) {
                minRatio = ratio;
                minRatioPositions.clear(); // Clear list and start fresh with new minimum
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.size() == 0) {
        return null;
    } else if (minRatioPositions.size() > 1) {
        // there's a degeneracy as indicated by a tie in the minimum ratio test

        // ... rest of the code remains unchanged ...
    }
    return minRatioPositions.get(0);
}