protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<Integer>();
    columnsToDrop.add(0);

    // positive cost non-artificial variables
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

    columnsToDrop.sort(Collections.reverseOrder()); // Sort in descending order for safe deletion

    double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
    for (int i = 1; i < getHeight(); i++) {
      int col = 0;
      for (int j = 0; j < getWidth(); j++) {
        if (!columnsToDrop.contains(j)) {
          matrix[i - 1][col++] = tableau.getEntry(i, j);
        }
      }
    }

    // Adjust column labels and artificial variable count in reversed order
    for (Integer colIndex : columnsToDrop) {
      columnLabels.remove(colIndex.intValue());
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables -= columnsToDrop.stream().filter(idx -> idx >= getArtificialVariableOffset()).count();
}
