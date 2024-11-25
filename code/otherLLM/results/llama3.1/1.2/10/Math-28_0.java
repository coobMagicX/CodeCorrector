private Integer getPivotRow(SimplexTableau tableau, final int col) {
    // create a list of all the rows that tie for the lowest score in the minimum ratio test
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            final double ratio = rhs / entry;
            // check if the entry is strictly equal to the current min ratio
            // do not use a ulp/epsilon check
            final int cmp = Double.compare(ratio, minRatio);
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) {
                minRatio = ratio;
                minRatioPositions = new ArrayList<Integer>();
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.size() == 0) {
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
        // Additional heuristic: if we did not get a solution after half of maxIterations
        //                       revert to the simple case of just returning the top-most row
        // This heuristic is based on empirical data gathered while investigating MATH-828.
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

            // Check for cycles in the constraint matrix
            if (checkForCycles(tableau, minRatioPositions)) {
                throw new ArithmeticException("Circular dependency detected in the constraint matrix");
            }

            return minRow;
    }
    return minRatioPositions.get(0);
}

private boolean checkForCycles(SimplexTableau tableau, List<Integer> rows) {
    int m = rows.size();
    int[] visited = new int[m];
    int[] stack = new int[m];

    for (int i = 0; i < m; i++) {
        if (!visited[i]) {
            if (hasCycle(tableau, rows, i, visited, stack)) {
                return true;
            }
        }
    }

    return false;
}

private boolean hasCycle(SimplexTableau tableau, List<Integer> rows, int currentIndex, int[] visited, int[] stack) {
    visited[currentIndex] = 1;
    stack[currentIndex] = 1;

    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        if (rows.contains(i)) {
            int index = i - tableau.getNumObjectiveFunctions();

            // Check if current row points to a row that is already in the recursion stack
            if (stack[index] == 1) {
                return true;
            }

            if (!visited[index]) {
                if (hasCycle(tableau, rows, index, visited, stack)) {
                    return true;
                }
            }
        }
    }

    // If we reach here, it means there's no cycle
    stack[currentIndex] = 0;

    return false;
}