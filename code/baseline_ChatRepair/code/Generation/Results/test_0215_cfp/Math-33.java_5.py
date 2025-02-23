protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<Integer>();
    columnsToDrop.add(0);  // Phase 1 objective function index

    // positive cost non-artificial variables
    for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, eps) > 0) {
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

    // Reverse sort to enable correct removal without affecting indices of columns yet to be removed
    Collections.sort(columnsToDrop, Collections.reverseOrder());

    // Drop columns from tableau and column labels
    for (int index : columnsToDrop) {
        dropColumnFromTableau(index);
    }
    
    // Reset the count of artificial variables
    this.numArtificialVariables = 0;
}

private void dropColumnFromTableau(int columnIndex) { 
    int numRows = tableau.getRowDimension(), numCols = tableau.getColumnDimension();
    double[][] newMatrix = new double[numRows][numCols - 1];

    for (int row = 0; row < numRows; row++) {
        for (int col = 0, newCol = 0; col < numCols; col++) {
            if (col != columnIndex) {
                newMatrix[row][newCol++] = tableau.getEntry(row, col);
            }
        }
    }
    tableau = new Array2DRowRealMatrix(newMatrix);
    columnLabels.remove(columnIndex); 
}
