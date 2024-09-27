protected RealPointValuePair getSolution() {
    int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
    Integer negativeVarBasicRow = negativeVarColumn >= 0 ? getBasicRow(negativeVarColumn) : null; // Fixed comparison to include column index 0
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
        if (basicRows.contains(basicRow)) {
            coefficients[i] = 0;
        } else {
            basicRows.add(basicRow);
            double entryValue = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
            coefficients[i] = entryValue - (restrictToNonNegative && entryValue < 0 ? mostNegative : 0);
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}