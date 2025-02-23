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
                minRatioPositions.clear();
                minRatioPositions.add(i);
                minRatio = ratio;
            } else if (comparison == 0) {
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
    
    
    int lowestValuedEnteringVariableRow = minRatioPositions.get(0);
    int smallestIndex = tableau.getEnteringVariable(lowestValuedEnteringVariableRow, col);
    for (int row : minRatioPositions) {
        int currentIndex = tableau.getEnteringVariable(row, col);
        if (currentIndex < smallestIndex) {
            smallestIndex = currentIndex;
            lowestValuedEnteringVariableRow = row;
        }
    }

    return lowestValuedEnteringVariableRow;
}
