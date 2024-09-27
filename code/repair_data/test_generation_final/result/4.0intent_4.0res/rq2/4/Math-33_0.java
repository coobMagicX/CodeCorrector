protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<Integer>();
    columnsToDrop.add(0); // Drop the first column (Phase 1 objective)

    // positive cost non-artificial variables
    for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
        final double entry = tableau.getEntry(0, i);
        // Only drop if the cost is positive
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            columnsToDrop.add(i);
        }
    }

    // non-basic artificial variables
    for (int i = 0; i < getNumArtificialVariables(); i++) {
        int col = i + getArtificialVariableOffset();
        // Check if the artificial variable is non-basic
        if (getBasicRow(col) == null) {
            columnsToDrop.add(col);
        }
    }

    // Ensuring that we only remove columns that are necessary and correctly maintaining the tableau's integrity
    if (columnsToDrop.size() > 0) {
        double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
        for (int i = 1; i < getHeight(); i++) {
            int colIndex = 0;
            for (int j = 0; j < getWidth(); j++) {
                if (!columnsToDrop.contains(j)) {
                    matrix[i - 1][colIndex++] = tableau.getEntry(i, j);
                }
            }
        }

        for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
            columnLabels.remove((int) columnsToDrop.get(i));
        }

        this.tableau = new Array2DRowRealMatrix(matrix);
        this.numArtificialVariables -= columnsToDrop.stream()
            .filter(col -> col >= getArtificialVariableOffset() && col < getArtificialVariableOffset() + getNumArtificialVariables())
            .count(); // Correctly update the number of artificial variables
    }
}