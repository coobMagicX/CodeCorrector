private Integer getPivotRow(SimplexTableau tableau, final int col) {
    List<Integer> minRatioPositions = new ArrayList<>();
    double minRatio = Double.MAX_VALUE;
    
    
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);

        
        if (Precision.compareTo(entry, 0.0, maxUlps) > 0) {
            double ratio = rhs / entry;
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

    
    if (minRatioPositions.size() > 1) {
        return applyBlandsRule(tableau, minRatioPositions);
    }
    
    
    return minRatioPositions.get(0);
}

private Integer applyBlandsRule(SimplexTableau tableau, List<Integer> candidates) {
    Integer rowWithSmallestIndex = null;
    int smallestIndex = Integer.MAX_VALUE;
    
    for (Integer row : candidates) {
        for (int columnIndex = 0; columnIndex < tableau.getWidth(); columnIndex++) {
            if (tableau.getBasicRow(columnIndex) == row) {
                if (columnIndex < smallestIndex) {
                    smallestIndex = columnIndex;
                    rowWithSmallestIndex = row;
                }
                break; 
            }
        }
    }
    
    return rowWithSmallestIndex;
}
