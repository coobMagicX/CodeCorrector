protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<Integer>();
    int artificialVariableOffset = getArtificialVariableOffset();

    // positive cost non-artificial variables
    for (int i = getNumObjectiveFunctions(); i < artificialVariableOffset; i++) {
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0 && !isArtificialVariable(i)) {
            columnsToDrop.add(i);
        }
    }

    // non-basic artificial variables
    for (int i = 0; i < getNumArtificialVariables(); i++) {
        int col = i + artificialVariableOffset;
        if (getBasicRow(col) == null && isArtificialVariable(col)) {
            columnsToDrop.add(col);
        }
    }

    double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
    for (int i = 1; i < getHeight(); i++) {
      int colIndex = 0;
      for (int j = 0; j < getWidth(); j++) {
        if (!columnsToDrop.contains(j)) {
          matrix[i - 1][colIndex++] = tableau.getEntry(i, j);
        }
      }
    }

    for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
      columnLabels.remove(columnsToDrop.get(i));
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = getNumArtificialVariables() - columnsToDrop.size();
}

// Helper method to check if a variable is artificial
private boolean isArtificialVariable(int index) {
    int startOfArtificialVariables = getArtificialVariableOffset();
    return index >= startOfArtificialVariables && index < getWidth();
}