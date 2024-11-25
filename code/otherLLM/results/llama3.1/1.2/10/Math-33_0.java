protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<Integer>();
    for (int i = getArtificialVariableOffset(); i < getWidth(); i++) {
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0 || getBasicRow(i) == null) {
            columnsToDrop.add(i);
        }
    }

    // Reorder columnsToDrop to have phase-1 objective variables first
    Collections.sort(columnsToDrop, Comparator.comparingInt(getArtificialVariableOffset()));

    double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
    int colIndex = 0;
    for (int i = 0; i < getWidth(); i++) {
        if (!columnsToDrop.contains(i)) {
            matrix[getHeight() - 2][colIndex++] = tableau.getEntry(getHeight() - 1, i);
        }
    }

    for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
      columnLabels.remove((int) columnsToDrop.get(i));
    }

    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
}