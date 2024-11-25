protected RealPointValuePair getSolution() {
    int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
    Integer negativeVarBasicRow = negativeVarColumn > 0 ? getBasicRow(negativeVarColumn) : null;
    double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());

    Set<Integer> basicRows = new HashSet<Integer>();
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    for (int i = 0; i < coefficients.length; i++) {
        int colIndex = columnLabels.indexOf("x" + i);
        if (colIndex < 0) {
            coefficients[i] = 0;
            continue;
        }
        Integer basicRow = getBasicRow(colIndex);

        // Check if the basic row is part of the objective function and is unconstrained
        if (basicRows.contains(basicRow) && !isConstraintVariable(colIndex)) {
            // Set the coefficient to 0 for unconstrained variables in the objective function
            coefficients[i] = 0;
        } else {
            basicRows.add(basicRow);
            double entryValue = getEntry(basicRow, getRhsOffset());
            coefficients[i] = (basicRow == null ? 0 : entryValue) - 
                               (isConstraintVariable(colIndex) && restrictToNonNegative ? mostNegative : 0);
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}

// Method to check if the column index corresponds to a constraint variable
private boolean isConstraintVariable(int colIndex) {
    // Assuming there's a method that determines whether a column is a constraint variable or not.
    // Replace this with the actual method you have.
    return false;
}