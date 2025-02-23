import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }

    List<Integer> columnsToDrop = new ArrayList<Integer>();
    columnsToDrop.add(0);  // Phase 1 objective function index

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

    Collections.sort(columnsToDrop, Collections.reverseOrder());  // Ensure we remove columns from right to left
    for (Integer colIndex : columnsToDrop) {
        tableau.dropColumn(colIndex);
        columnLabels.remove(colIndex.intValue());
    }

    this.numArtificialVariables = 0;
}

private void dropColumn(int colIndexToRemove) {
    int numRows = tableau.getRowDimension();
    int numCols = tableau.getColumnDimension();
    double[][] newData = new double[numRows][numCols - 1];

    for (int i = 0; i < numRows; i++) {
        for (int j = 0, newCol = 0; j < numCols; j++) {
            if (j != colIndexToRemove) {
                newData[i][newCol++] = tableau.getEntry(i, j);
            }
        }
    }

    this.tableau = new Array2DRowRealMatrix(newData);
}
