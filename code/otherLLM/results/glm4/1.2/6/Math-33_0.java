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

    double[][] matrix = new double[getHeight() - columnsToDrop.size()][getWidth()];
    for (int i = 0; i < getHeight(); i++) {
        int row = i;
        for (int j = 0; j < getWidth(); j++) {
            if (!columnsToDrop.contains(j)) {
                matrix[row][j] = tableau.getEntry(i, j);
            }
        }
    }

    // Remove columns from the column labels list
    Collections.reverse(columnsToDrop); // Reverse to remove from right to left
    for (int colIndex : columnsToDrop) {
        columnLabels.remove((int) colIndex);
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
}