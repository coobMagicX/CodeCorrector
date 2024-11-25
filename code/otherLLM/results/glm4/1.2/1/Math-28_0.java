import java.util.ArrayList;
import java.util.List;

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
        System.out.println("Degeneracy detected. Multiple rows with minimum ratio:");
        for (Integer row : minRatioPositions) {
            System.out.println("Row " + row + ": Ratio = " + tableau.getEntry(row, col) / tableau.getEntry(row, tableau.getWidth() - 1));
        }

        // Check if there's an artificial variable that can be forced out of the basis
        for (Integer row : minRatioPositions) {
            for (int i = 0; i < tableau.getNumArtificialVariables(); i++) {
                int column = i + tableau.getArtificialVariableOffset();
                final double entry = tableau.getEntry(row, column);
                if (Precision.equals(entry, 1d, maxUlps) && row.equals(tableau.getBasicRow(column))) {
                    return row;
                }
            }
        }

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

        // Additional heuristic: If no solution after half of maxIterations, revert to the top-most row
        if (minRow == null) {
            int halfOfMaxIterations = tableau.getMaxIterations() / 2;
            for (Integer row : minRatioPositions) {
                if (tableau.getIterationCount() < halfOfMaxIterations || minIndex > tableau.getNumObjectiveFunctions()) {
                    minIndex = Math.min(minIndex, tableau.getNumObjectiveFunctions());
                    minRow = row;
                }
            }
        }

        System.out.println("Chosen pivot row: " + minRow);
        return minRow;
    }
    return minRatioPositions.get(0);
}