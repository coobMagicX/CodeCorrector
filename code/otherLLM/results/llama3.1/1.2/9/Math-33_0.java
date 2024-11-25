protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<Integer>();
    for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            columnsToDrop.add(i);
        }
    }

    // non-basic artificial variables
    for (int i = 0; i < getNumArtificialVariables(); i++) {
      int col = i + getArtificialVariableOffset();
      if (getBasicRow(col) == null) {
        columnsToDrop.add(col);
      }
    }

    double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
    for (int i = 1; i < getHeight(); i++) {
      int col = 0;
      for (int j = 0; j < getWidth(); j++) {
        if (!columnsToDrop.contains(j)) {
          matrix[i - 1][col++] = tableau.getEntry(i, j);
        }
      }
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    
    List<String> labelsToRemove = new ArrayList<>();
    for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
      labelsToRemove.add(columnLabels.get(columnsToDrop.get(i)));
      columnLabels.remove((Integer)columnsToDrop.get(i));
    }

    this.columnLabels.removeAll(labelsToRemove);
    
    // Update numArtificialVariables after removing columns
    int offset = getArtificialVariableOffset();
    for (int i = 0; i < getWidth() - columnsToDrop.size(); i++) {
      if (columnLabels.contains(String.valueOf(i))) {
        if (i >= offset) {
          this.numArtificialVariables++;
        }
      } else {
        columnLabels.set(i, String.valueOf(i));
      }
    }

    setColumnLabels(columnLabels);
}