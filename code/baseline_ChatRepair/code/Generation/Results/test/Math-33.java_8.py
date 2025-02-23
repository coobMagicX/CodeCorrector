protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return; // If only one objective function exists, no dropping is necessary.
    }
    
    List<Integer> columnsToDrop = new ArrayList<Integer>();
    columnsToDrop.add(0); // Drop the phase 1 objective function column.

    // Identify positive cost non-artificial variables
    for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            columnsToDrop.add(i);
        }
    }

    // Identify non-basic artificial variables to drop
    for (int i = getNumArtificialVariables() - 1; i >= 0; i--) {
        int col = i + getArtificialVariableOffset();
        if (getBasicRow(col) == -1) {  // If there's no basic row for this column
            columnsToDrop.add(col);
        }
    }

    // Copy over the matrix excluding the columns to drop
    double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
    for (int i = 1; i < getHeight(); i++) {
        int col = 0;
        for (int j = 0; j < getWidth(); j++) {
            if (!columnsToDrop.contains(j)) {
                matrix[i - 1][col++] = tableau.getEntry(i, j);
            }
        }
    }

    // Sort the columns to drop in descending order and remove columns from labels
    Collections.sort(columnsToDrop, Collections.reverseOrder());
    for (Integer colIndex : columnsToDrop) {
        columnLabels.remove(colIndex.intValue());
    }

    // Update the tableau with the new matrix (excluding dropped columns)
    this.tableau = new Array2DRowRealMatrix(matrix);
    // Reset number of artificial variables as all have been dropped
    this.numArtificialVariables = 0;
}
