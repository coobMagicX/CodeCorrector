protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    double[] rhsEntries = new double[coefficients.length];

    // Initialize coefficients and collect RHS entries
    for (int i = 0; i < coefficients.length; i++) {
        coefficients[i] = 0.0;
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            rhsEntries[i] = getEntry(basicRow, getRhsOffset());
        } else {
            rhsEntries[i] = 0.0;
        }
    }

    // Determine the coefficients based on rhsEntries correctness
    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            boolean isOnlyOneInColumn = true;
            // Check if this column (variable index) is the only one that has non-zero in this row
            for (int j = 0; j < coefficients.length; j++) {
                if (i != j && tableau.getEntry(basicRow, getNumObjectiveFunctions() + j) != 0) {
                    isOnlyOneInColumn = false;
                    break;
                }
            }
            if (isOnlyOneInColumn) {
                coefficients[i] = rhsEntries[i];  // Assure this coefficient is set correctly if it's uniquely identified in the row
            }
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
