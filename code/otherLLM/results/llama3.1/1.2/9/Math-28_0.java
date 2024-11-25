private Integer getPivotRow(SimplexTableau tableau, final int col) {
    List<Integer> minRatioPositions = new ArrayList<>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            double ratio = rhs / entry;
            int cmp = Double.compare(ratio, minRatio);
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) {
                minRatio = ratio;
                minRatioPositions.clear();
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.size() == 0) {
        return null;
    } else if (minRatioPositions.size() > 1) {
        // Sort degenerate rows by objective function coefficients
        Collections.sort(minRatioPositions, Comparator.comparingDouble(
                row -> tableau.getEntry(row, col)));

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

        return minRow;
    } else {
        // Return the single degenerate row
        return minRatioPositions.get(0);
    }
}