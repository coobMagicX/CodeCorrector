private Integer getPivotRow(SimplexTableau tableau, final int col) {
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            final double ratio = rhs / entry;

            
            if (Double.compare(ratio, minRatio) < 0) {
                minRatio = ratio;
                minRatioPositions.clear();
                minRatioPositions.add(i);
            } 
            
            else if (Double.compare(ratio, minRatio) == 0) {
                minRatioPositions.add(i);
            }
        }
    }

    if (minRatioPositions.isEmpty()) {
        return null; 
    }

    if (minRatioPositions.size() == 1) {
        
        return minRatioPositions.get(0);
    } else {
        
        int rowIndex = -1;
        int smallestIndexOfBasicVar = Integer.MAX_VALUE;
        for (Integer row : minRatioPositions) {
            int varIndex = tableau.getBasicVariableForRow(row);
            if (varIndex < smallestIndexOfBasicVar) {
                smallestIndexOfBasicVar = varIndex;
                rowIndex = row;
            }
        }
        return rowIndex;
    }
}
