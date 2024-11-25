protected RealPointValuePair getSolution() {
    int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
    Integer negativeVarBasicRow = negativeVarColumn > 0 ? getBasicRow(negativeVarColumn) : null;
    double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());

    Set<Integer> basicRows = new HashSet<>();
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    
    for (int i = 0; i < coefficients.length; i++) {
        int colIndex = columnLabels.indexOf("x" + i);
        if (colIndex < 0) {
            coefficients[i] = 0;
            continue;
        }
        Integer basicRow = getBasicRow(colIndex);

        // Check if the variable is in the objective function
        if ((negativeVarColumn != null && colIndex == negativeVarColumn)) {
            // If it is and restrictToNonNegative is true, set coefficient to 0
            coefficients[i] = restrictToNonNegative ? 0 : mostNegative;
        } else if (basicRow == null) {
            // If the basic row is null, set coefficient to 0
            coefficients[i] = 0;
        } else {
            // Otherwise, calculate the coefficient for this variable
            double entryValue = getEntry(basicRow, getRhsOffset());
            coefficients[i] = restrictToNonNegative ? entryValue : entryValue - mostNegative;
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}