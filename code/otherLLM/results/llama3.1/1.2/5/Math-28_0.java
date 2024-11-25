public PointValuePair doOptimize()
    throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
    final SimplexTableau tableau =
        new SimplexTableau(getFunction(),
                           getConstraints(),
                           getGoalType(),
                           restrictToNonNegative(),
                           epsilon,
                           maxUlps);

    solvePhase1(tableau);
    tableau.dropPhase1Objective();

    while (!tableau.isOptimal()) {
        // Check if the problem is degenerate
        List<Integer> minRatioPositions = new ArrayList<>();
        double minRatio = Double.MAX_VALUE;
        for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
            final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
            final double entry = tableau.getEntry(i, getBasicVariableColumn());
            
            if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
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

        if (minRatioPositions.size() == 0) {
            // No pivot row found, use Bland's rule
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
            
            return getMinPivotRow(tableau, minRow);
        } else if (minRatioPositions.size() > 1) {
            // There's a degeneracy as indicated by a tie in the minimum ratio test
            Integer pivotRow = null;
            
            for (Integer row : minRatioPositions) {
                for (int i = 0; i < tableau.getNumArtificialVariables(); i++) {
                    int column = i + tableau.getArtificialVariableOffset();
                    
                    final double entry = tableau.getEntry(row, column);
                    
                    if (Precision.equals(entry, 1d, maxUlps) && row == tableau.getBasicRow(column)) {
                        pivotRow = row;
                        break;
                    }
                }
                
                if (pivotRow != null) break;
            }
            
            if (pivotRow != null) return pivotRow;
        }

        // Apply Bland's rule to prevent cycling
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
        
        return getMinPivotRow(tableau, minRow);
    }
    
    // If no degeneracy is found, just return the top-most row
    Integer pivotRow = null;
    
    for (Integer row : minRatioPositions) {
        if (pivotRow == null || row < pivotRow) {
            pivotRow = row;
        }
    }
    
    return getMinPivotRow(tableau, pivotRow);
}

private PointValuePair getMinPivotRow(SimplexTableau tableau, Integer pivotRow) {
    // Apply Bland's rule
    int minIndex = tableau.getWidth();
    
    for (Integer row : (pivotRow == null ? new ArrayList<>() : Arrays.asList(pivotRow))) {
        int i = tableau.getNumObjectiveFunctions();
        
        for (; i < tableau.getWidth() - 1 && row != pivotRow; i++) {
            if (row == tableau.getBasicRow(i)) {
                if (i < minIndex) {
                    minIndex = i;
                    pivotRow = row;
                }
            }
        }
    }
    
    return getMinPivotRow(tableau, pivotRow);
}

private PointValuePair getMinPivotRow(SimplexTableau tableau, Integer pivotRow) {
    // Get the column of the basic variable
    int basicVariableColumn = getBasicVariableColumn();
    
    for (int i = 0; i < tableau.getWidth() - 1 && pivotRow == null; i++) {
        if (tableau.getBasicRow(i) == pivotRow) {
            basicVariableColumn = i;
            break;
        }
    }
    
    // Get the minimum ratio
    double minRatio = Double.MAX_VALUE;
    
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, basicVariableColumn);
        
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            final double ratio = rhs / entry;
            
            if (ratio < minRatio) {
                minRatio = ratio;
                pivotRow = i;
            }
        }
    }
    
    return getMinPivotRow(tableau, pivotRow);
}

private int getBasicVariableColumn() {
    // Get the column of the basic variable
    // ...
}