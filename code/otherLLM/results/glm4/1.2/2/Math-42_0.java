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

        // Check if the current variable is the negative variable and apply non-negativity constraint
        if (negativeVarColumn == colIndex && restrictToNonNegative) {
            coefficients[i] = -mostNegative; // Apply the most negative value from the right-hand side
        } else {
            // If not the negative variable, calculate the coefficient normally
            coefficients[i] =
                (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) -
                (restrictToNonNegative && basicRows.contains(basicRow) ? mostNegative : 0);
        }

        if (basicRows.add(basicRow)) {
            // Add the basic row to the set only once
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}