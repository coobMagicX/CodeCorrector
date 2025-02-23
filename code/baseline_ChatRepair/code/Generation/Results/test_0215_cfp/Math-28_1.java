private Integer getPivotRow(SimplexTableau tableau, final int col) {
    List<Integer> minRatioPositions = new ArrayList<>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            final double ratio = rhs / entry;
            int cmp = Double.compare(ratio, minRatio);
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) {
                minRatioPositions.clear();
                minRatioPositions.add(i);
                minRatio = ratio;
            }
        }
    }

    if (minRatioPositions.isEmpty()) {
        return null; 
    }

    
    if (minRatioPositions.size() > 1) {
        Integer selectedRow = minRatioPositions.get(0);
        int minIndex = tableau.getWidth(); 

        for (Integer row : minRatioPositions) {
            
            int basicVarIndex = findBasicVariableIndex(tableau, row);
            if (basicVarIndex != -1 && basicVarIndex < minIndex) {
                minIndex = basicVarIndex;
                selectedRow = row;
            }
        }
        return selectedRow;
    }

    
    return minRatioPositions.get(0);
}

private int findBasicVariableIndex(SimplexTableau tableau, int row) {
    
    for (int col = 0; col < tableau.getWidth(); col++) {
        boolean isBasic = tableau.getEntry(row, col) == 1d;
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != row && tableau.getEntry(i, col) != 0d) {
                isBasic = false;
                break;
            }
        }
        if (isBasic) {
            return col; 
        }
    }
    return -1; 
}
