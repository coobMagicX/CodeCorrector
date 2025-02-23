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
        if (basicRow == null) {
            coefficients[i] = 0;  // If no basic row exists for the column, coefficient remains 0
        } else {
            double coefValue = getEntry(basicRow, getRhsOffset());
            if (basicRows.contains(basicRow)) {
                // If a basic row is already represented, we shouldn't double-count its contribution
                coefficients[i] = 0;
            } else {
                basicRows.add(basicRow);
                if (restrictToNonNegative) {
                    coefficients[i] = Math.max(coefValue - mostNegative, 0);
                } else {
                    coefficients[i] =  coefValue - mostNegative;
                }
            }
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
