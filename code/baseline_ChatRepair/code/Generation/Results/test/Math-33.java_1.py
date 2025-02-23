protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    Set<Integer> columnsToDrop = new HashSet<Integer>();
    columnsToDrop.add(0);  // Drop the phase 1 objective row

    // Positive cost non-artificial variables
    for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            columnsToDrop.add(i);
        }
    }

    // Non-basic artificial variables
    for (int i = 0; i < getNumArtificialVariables(); i++) {
        int col = i + getArtificialVariableOffset();
        if (getBasicRow(col) == null) {
            columnsToDrop.add(col);
        }
    }

    // Create a new matrix without the dropped columns
    double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
    for (int i = 1; i < getHeight(); i++) {
        int colIndex = 0;
        for (int j = 0; j < getWidth(); j++) {
            if (!columnsToDrop.contains(j)) {
                matrix[i - 1][colIndex++] = tableau.getEntry(i, j);
            }
        }
    }

    // Remove the labels of the dropped columns
    List<Integer> sortedColumnsToDrop = new ArrayList<Integer>(columnsToDrop);
    Collections.sort(sortedColumnsToDrop, Collections.reverseOrder());
    for (int col : sortedColumnsToDrop) {
        columnLabels.remove(col);
    }

    // Set the new tableau and reset the number of artificial variables
    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
}
