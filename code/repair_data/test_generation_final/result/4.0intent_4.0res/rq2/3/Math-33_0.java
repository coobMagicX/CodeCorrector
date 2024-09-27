protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<Integer>();
    columnsToDrop.add(0); // drop the first row that represents the phase 1 objective function

    // positive cost non-artificial variables
    for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) { // Change back to drop variables with positive cost
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

    // Remove columns from the end to avoid index shifting problems
    for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
        columnLabels.remove((int) columnsToDrop.get(i));
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables -= columnsToDrop.size(); // update the count of artificial variables properly
}