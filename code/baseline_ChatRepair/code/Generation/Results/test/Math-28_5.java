private Integer getPivotRow(SimplexTableau tableau, final int col) {
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0, maxUlps) > 0) {
            final double ratio = rhs / entry;
            int comparison = Double.compare(ratio, minRatio);
            if (comparison < 0) { 
                minRatio = ratio;
                minRatioPositions.clear(); 
                minRatioPositions.add(i); 
            } else if (comparison == 0) { 
                minRatioPositions.add(i); 
            }
        }
    }

    if (minRatioPositions.isEmpty()) {
        return null; 
    }

    
    Integer pivotRow = minRatioPositions.get(0);
    for (Integer row : minRatioPositions) {
        if (tableau.getBasicVariable(row) < tableau.getBasicVariable(pivotRow)) {
            pivotRow = row;
        }
    }
    
    return pivotRow;
}
