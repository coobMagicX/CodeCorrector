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

        // Check for the objective function row and handle unconstrained variables
        if (basicRows.contains(basicRow)) {
            coefficients[i] = 0; // Set to zero as these are part of the objective function
        } else {
            basicRows.add(basicRow);
            double entryValue = getEntry(basicRow, getRhsOffset());
            
            // If restricting to non-negative and the variable is not in the objective function,
            // adjust the coefficient accordingly.
            if (restrictToNonNegative && colIndex != negativeVarColumn) {
                coefficients[i] = entryValue - mostNegative;
            } else {
                coefficients[i] = entryValue;
            }
        }
    }

    // Ensure that the variable with the label "NEGATIVE_VAR_COLUMN_LABEL" is non-negative
    if (negativeVarColumn >= 0 && restrictToNonNegative) {
        double currentCoefficient = Math.min(coefficients[negativeVarColumn], 0);
        coefficients[negativeVarColumn] -= currentCoefficient;
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}