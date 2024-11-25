protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        // If we have only one objective function, it might be the case of artificial variables,
        // so we need to handle this special case properly.
        double entry = tableau.getEntry(0, 0);
        if (Precision.compareTo(entry, 0d, maxUlps) <= 0) {
            // If the first element is not positive, we might have a phase-1 problem without artificial variables.
            // In such cases, there's nothing to drop as we are already in phase-2 after initial artificial variables
            // were converted to basic variables. Thus, we can return early.
            return;
        }
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

    double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
    for (int i = 0; i < getHeight() - 1; i++) { // Adjusted to start from 0
      int col = 0;
      for (int j = 0; j < getWidth(); j++) {
        if (!columnsToDrop.contains(j)) {
          matrix[i][col++] = tableau.getEntry(i + 1, j); // Adjusted to use i+1 since arrays are 0-indexed
        }
      }
    }

    for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
      columnLabels.remove(columnsToDrop.get(i)); // Use the index from the list directly
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
}