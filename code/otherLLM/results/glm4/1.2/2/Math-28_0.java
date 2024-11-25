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

    if (minRatioPositions.size() == 0) {
        return null;
    } else if (minRatioPositions.size() > 1) {
        // Apply Bland's rule to prevent cycling
        Integer minRow = null;
        int minIndex = tableau.getWidth();
        for (Integer row : minRatioPositions) {
            int i = tableau.getNumObjectiveFunctions();
            for (; i < tableau.getWidth() - 1 && minRow == null; i++) {
                if (row == tableau.getBasicRow(i)) {
                    if (i < minIndex) {
                        minIndex = i;
                        minRow = row;
                    }
                }
            }
        }

        // If no suitable pivot element is found, revert to the top-most row
        if (minRow == null) {
            minRow = minRatioPositions.get(0);
        }
    } else {
        return minRatioPositions.get(0);
    }

    return minRow;
}