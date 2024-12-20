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
        
        // Check if the variable is associated with a basic row
        boolean isVariableInBaseForm = basicRow != null && basicRows.add(basicRow);

        double entryValue = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset()));
        coefficients[i] = restrictToNonNegative ? entryValue : entryValue - mostNegative;

        // If the variable is not in a basic form, it could be part of the objective function
        if (!isVariableInBaseForm && !basicRows.contains(negativeVarBasicRow)) {
            coefficients[i] -= mostNegative;
        }
    }
    
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}