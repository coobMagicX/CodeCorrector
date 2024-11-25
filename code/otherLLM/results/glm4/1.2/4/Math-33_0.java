protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<>();
    // First, drop all non-positive cost objective functions
    for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) <= 0) { // Modified condition to include zero
            columnsToDrop.add(i);
        }
    }

    // Next, drop non-basic artificial variables
    for (int i = 0; i < getNumArtificialVariables(); i++) {
        int col = i + getArtificialVariableOffset();
        if (getBasicRow(col) == null && Precision.compareTo(tableau.getEntry(0, col), 0d, maxUlps) > 0) { // Check if the cost is positive
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

    for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
        int colToRemove = (int) columnsToDrop.get(i);
        columnLabels.remove(colToRemove); // Ensure the column label is also removed
    }

    this.tableau = new Array2DRowRealMatrix(matrix, false); // Use false to avoid copying array
    updateNumArtificialVariables(); // Update number of artificial variables accordingly
}

private void updateNumArtificialVariables() {
    int numArtificials = 0;
    for (int col : columnLabels) {
        if (col < getArtificialVariableOffset()) {
            numArtificials++;
        }
    }
    this.numArtificialVariables = numArtificials;
}