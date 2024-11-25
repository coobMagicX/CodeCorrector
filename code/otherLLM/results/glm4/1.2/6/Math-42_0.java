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

        // Check for the objective function row
        if (basicRows.contains(basicRow)) {
            // If multiple variables can take a given value, choose the first and set the rest equal to 0
            coefficients[i] = 0 - (restrictToNonNegative ? 0 : mostNegative);
        } else {
            basicRows.add(basicRow);

            // Calculate the coefficient for the variable, ensuring non-negative values if restrictToNonNegative is true
            double value = getEntry(basicRow, getRhsOffset());
            coefficients[i] = value - (restrictToNonNegative ? 0 : mostNegative);
        }

        // Ensure that the coefficient does not become negative due to subtraction of mostNegative
        if (coefficients[i] < 0) {
            coefficients[i] = 0;
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}