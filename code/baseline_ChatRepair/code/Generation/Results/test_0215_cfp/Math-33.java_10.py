protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return; // If there is only one objective function, there is no need to drop any columns.
    }

    List<Integer> columnsToDrop = new ArrayList<>();
    columnsToDrop.add(0); // Always drop the first column completing the phase 1 part of the tableau.

    // positive cost non-artificial variables
    for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
        double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            columnsToDrop.add(i); // Add columns with positive cost to drop list.
        }
    }

    // non-basic artificial variables
    for (int i = 0; i < getNumArtificialVariables(); i++) {
        int col = i + getArtificialVariableOffset(); // Calculating column index of the artificial variable.
        if (getBasicRow(col) == null) {
            columnsToDrop.add(col); // Add non-basic artificial variables to drop list.
        }
    }

    Collections.sort(columnsToDrop, Collections.reverseOrder()); // Sort in descending order for safe removal.

    // Creating new matrix of smaller size by dropping columns.
    double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
    for (int i = 1; i < getHeight(); i++) {
        int colIndex = 0;
        for (int j = 0; j < getWidth(); j++) {
            if (!columnsToDrop.contains(j)) {
                matrix[i - 1][colIndex++] = tableau.getEntry(i, j); // Copying non-dropped columns.
            }
        }
    }

    // Properly removing labels by iterating the sorted list of to-be-dropped columns.
    for (Integer colIndex : columnsToDrop) {
        columnLabels.remove(colIndex.intValue());
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables -= (int) columnsToDrop.stream().filter(idx -> idx >= getArtificialVariableOffset()).count(); // Adjusting the count of artificial variables.
}
