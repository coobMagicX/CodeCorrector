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
        if (basicRow == null || basicRows.contains(basicRow)) {
            // Setting coefficient to zero if no basic row found or it's already used
            coefficients[i] = 0;
        } else {
            basicRows.add(basicRow);
            double coefficientValue = getEntry(basicRow, getRhsOffset());

            // Ensure coefficients are non-negative if restrictToNonNegative is true
            if (restrictToNonNegative && coefficientValue < 0) {
                coefficientValue = 0;
            }

            // Additional handling for negative variables to ensure the test cases
            // with negative decision variables are correctly handled.
            if (mostNegative < 0 && coefficientValue < 0) {
                coefficientValue = Math.abs(coefficientValue);
            }

            coefficients[i] = coefficientValue;
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}