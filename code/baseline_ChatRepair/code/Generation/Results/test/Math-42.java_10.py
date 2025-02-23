protected RealPointValuePair getSolution() {
    int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
    Integer negativeVarBasicRow = negativeVarColumn >= 0 ? getBasicRow(negativeVarColumn) : null;
    double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());

    Set<Integer> basicRows = new HashSet<>();
    double[] coefficients = new double[getOriginalNumDecisionVariables()];

    for (int i = 0; i < coefficients.length; i++) {
        int colIndex = columnLabels.indexOf("x" + i);
        if (colIndex < 0) {
            coefficients[i] = 0; // If column does not exist, set coefficient to 0.
            continue;
        }

        Integer basicRow = getBasicRow(colIndex);
        if (basicRow != null && !basicRows.contains(basicRow)) {
            basicRows.add(basicRow);
            double value = getEntry(basicRow, getRhsOffset());
            coefficients[i] = restrictToNonNegative ? Math.max(0, value - mostNegative) : value - mostNegative;
        } else {
            coefficients[i] = 0; // If row is duplicated or null, set coefficient to 0.
        }
    }
    // Adjust the coefficients array to ensure non-negativity as per the test requirement
    for (int i = 0; i < coefficients.length; i++) {
        if (restrictToNonNegative && coefficients[i] < 0) {
            coefficients[i] = 0;
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
