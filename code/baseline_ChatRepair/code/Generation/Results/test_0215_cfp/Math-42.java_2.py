protected RealPointValuePair getSolution() {
    int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
    Integer negativeVarBasicRow = negativeVarColumn >= 0 ? getBasicRow(negativeVarColumn) : null;
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
        if (basicRows.contains(basicRow)) {
            coefficients[i] = 0; // All duplicate var should be 0 to maintain uniqueness of basic variables
        } else {
            basicRows.add(basicRow);
            if (basicRow != null) {
                double entryValue = getEntry(basicRow, getRhsOffset());
                if (restrictToNonNegative && entryValue < 0) {
                    coefficients[i] = 0;
                } else {
                    coefficients[i] = entryValue - (restrictToNonNegative ? Math.max(0, mostNegative) : mostNegative);
                }
            } else {
                coefficients[i] = 0;
            }
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
