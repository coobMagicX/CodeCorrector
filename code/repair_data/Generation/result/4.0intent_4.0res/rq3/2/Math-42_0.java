protected RealPointValuePair getSolution() {
    int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
    Integer negativeVarBasicRow = negativeVarColumn >= 0 ? getBasicRow(negativeVarColumn) : null;
    double mostNegative = negativeVarBasicRow != null ? getEntry(negativeVarBasicRow, getRhsOffset()) : 0;

    Set<Integer> basicRows = new HashSet<Integer>();
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    for (int i = 0; i < coefficients.length; i++) {
        int colIndex = columnLabels.indexOf("x" + i);
        if (colIndex < 0) {
            coefficients[i] = 0;
            continue;
        }
        Integer basicRow = getBasicRow(colIndex);
        if (basicRow != null && !basicRows.add(basicRow)) {
            // if the basic row is already used by another variable, set this coefficient to zero
            coefficients[i] = 0;
        } else {
            // set coefficient based on the right-hand side value minus any adjustment for negativity
            coefficients[i] = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0 : mostNegative);
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}