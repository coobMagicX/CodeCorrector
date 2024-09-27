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

    double[][] matrix = new double[getHeight()][getWidth() - columnsToDrop.size()];
    int row = 0;
    for (int i = 0; i < getHeight(); i++) {
        if (!columnsToDrop.contains(i)) {
            int col = 0;
            for (int j = 0; j < getWidth(); j++) {
                if (!columnsToDrop.contains(j)) {
                    matrix[row][col++] = tableau.getEntry(i, j);
                }
            }
            row++;
        }
    }

    for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
        columnLabels.remove((int) columnsToDrop.get(i));
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
}