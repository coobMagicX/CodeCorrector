private Integer getPivotRow(SimplexTableau tableau, final int col) {
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {  
            final double ratio = rhs / entry;
            int cmp = Double.compare(ratio, minRatio);
            if (cmp < 0) {
                minRatio = ratio;
                minRatioPositions.clear();
                minRatioPositions.add(i);
            } else if (cmp == 0) {
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

    
    
    for (Integer row : minRatioPositions) {
        for (int i = 0; i < tableau.getNumArtificialVariables(); i++) {
            int column = i + tableau.getArtificialVariableOffset();
            if (Precision.equals(tableau.getEntry(row, column), 1d, maxUlps) && row.equals(tableau.getBasicRow(column))) {
                return row;  
            }
        }
    }

    
    Integer minRow = minRatioPositions.get(0);
    for (Integer row : minRatioPositions) {
        if (row < minRow) {
            minRow = row;
        }
    }
    return minRow;
}
