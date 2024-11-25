protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<Integer>();
    // Add artificial variable columns to the list of columns to drop
    for (int i = 0; i < getNumArtificialVariables(); i++) {
        columnsToDrop.add(i);
    }
    
    // Remove objective function column (if exists)
    int firstObjectiveFunctionColumn = getObjectiveFunctionOffset();
    if (firstObjectiveFunctionColumn != -1) {
        columnsToDrop.add(firstObjectiveFunctionColumn);
    }

    // positive cost non-artificial variables
    for (int i = getNumArtificialVariables(); i < getArtificialVariableOffset(); i++) {
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            columnsToDrop.add(i);
        }
    }

    // non-basic artificial variables
    for (int i = 0; i < getNumArtificialVariables(); i++) {
      int col = i + getArtificialVariableOffset();
      if (getBasicRow(col) == null && Precision.compareTo(getTableauEntry(0, col), 0d, maxUlps) > 0) { // Check for positive cost
        columnsToDrop.add(col);
      }
    }

    double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
    int newRow = 0;
    for (int i = 1; i < getHeight(); i++) {
      int newCol = 0;
      for (int j = 0; j < getWidth(); j++) {
        if (!columnsToDrop.contains(j)) {
          matrix[newRow][newCol++] = tableau.getEntry(i, j);
        }
      }
      newRow++;
    }

    Collections.reverse(columnsToDrop); // Reverse to remove columns from the beginning of the list
    for (int column : columnsToDrop) {
        columnLabels.remove(column); // Remove columns in reverse order
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
}