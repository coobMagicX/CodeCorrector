protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<>();
    columnsToDrop.add(0);

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
      if (getBasicRow(col) == null) {
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

    // Adjust the column indices to reflect the removed columns
    for (Integer col : columnsToDrop) {
        int rowIndex = getBasicRow(col);
        if (rowIndex != null) {
            double entry = tableau.getEntry(rowIndex, 0);
            if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
                // Subtract the value from the basic variable in the new matrix
                for (int i = 1; i < getHeight(); i++) {
                    matrix[i][colIndex] -= tableau.getEntry(i, col);
                }
            } else if (Precision.compareTo(entry, 0d, maxUlps) < 0) {
                // Adjust the column index to account for non-basic variables
                throw new IllegalArgumentException("Basic variable has negative value which is not allowed.");
            }
        }
    }

    for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
      columnLabels.remove(columnsToDrop.get(i));
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
}