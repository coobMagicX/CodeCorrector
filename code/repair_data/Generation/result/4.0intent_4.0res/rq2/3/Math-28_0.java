private Integer getPivotRow(SimplexTableau tableau, final int col) {
    // create a list of all the rows that tie for the lowest score in the minimum ratio test
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
                minRatioPositions.clear(); // Clear the list before adding the new minimum
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.isEmpty()) {
        return null;
    } else if (minRatioPositions.size() > 1) {
        // there's a degeneracy as indicated by a tie in the minimum ratio test
        // 1. check if there's an artificial variable that can be forced out of the basis
        for (Integer row : minRatioPositions) {
            for (int i = 0; i < tableau.getNumArtificialVariables(); i++) {
                int column = i + tableau.getArtificialVariableOffset();
                final double entry = tableau.getEntry(row, column);
                if (Precision.equals(entry, 1d, maxUlps) && row.equals(tableau.getBasicRow(column))) {
                    return row;
                }
            }
        }

        // 2. apply Bland's rule to prevent cycling:
        //    take the row for which the corresponding basic variable has the smallest index
        //
        // see http://www.stanford.edu/class/msande310/blandrule.pdf
        // see http://en.wikipedia.org/wiki/Bland%27s_rule (not equivalent to the above paper)
        //
        Integer minRow = null;
        int minIndex = Integer.MAX_VALUE;
        for (Integer row : minRatioPositions) {
            for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getWidth() - 1; i++) {
                if (row.equals(tableau.getBasicRow(i))) {
                    if (i < minIndex) {
                        minIndex = i;
                        minRow = row;
                    }
                }
            }
        }
        if (minRow == null) {
            // As a fallback when Bland's rule doesn't find a pivot, pick the first in the list
            return minRatioPositions.get(0);
        }
        return minRow;
    }
    return minRatioPositions.get(0);
}