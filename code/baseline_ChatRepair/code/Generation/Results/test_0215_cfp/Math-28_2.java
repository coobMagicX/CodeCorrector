private Integer getPivotRow(SimplexTableau tableau, final int col) {
    List<Integer> minRatioPositions = new ArrayList<>();
    double minRatio = Double.MAX_VALUE;

    
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
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
    } else if (minRatioPositions.size() == 1) {
        
        return minRatioPositions.get(0);
    } else {
        
        return applyBlandsRule(minRatioPositions, tableau);
    }
}

private Integer applyBlandsRule(List<Integer> minRatioPositions, SimplexTableau tableau) {
    Integer rowWithSmallestIndex = null;
    int smallestIndex = Integer.MAX_VALUE;    
    for (Integer row : minRatioPositions) {
        for (int i = 0; i < tableau.getWidth(); i++) {
            if (tableau.getBasicRow(i) == row && i < smallestIndex) {
                smallestIndex = i;
                rowWithSmallestIndex = row;
            }
        }
    }
    return rowWithSmallestIndex;
}
