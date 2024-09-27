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
        if (basicRow != null) {
            if (basicRows.contains(basicRow)) {
                coefficients[i] = 0 - (restrictToNonNegative ? 0 : mostNegative);
            } else {
                basicRows.add(basicRow);
                coefficients[i] = getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0 : mostNegative);
            }
        } else {
            coefficients[i] = 0;
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}