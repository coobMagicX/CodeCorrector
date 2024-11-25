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

        // If the variable is in the objective function and not constrained to non-negative
        if (basicRow != null && !isConstrainedToNonNegative(basicRow)) {
            coefficients[i] = -mostNegative;
        } else {
            coefficients[i] =
                (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) -
                (isConstrainedToNonNegative(basicRow) ? mostNegative : 0);
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}

private boolean isConstrainedToNonNegative(Integer basicRow) {
    // Assuming there's a method that determines if the variable associated with this row is constrained to be non-negative.
    // If no such method exists, replace it with appropriate logic or remove this check as necessary.
    return getConstraintType(basicRow).equals(CONSTRAINT_NON_NEGATIVE);
}

// Method to get constraint type (assuming it's defined in the class)
private String getConstraintType(Integer basicRow) {
    // Placeholder implementation; replace with actual logic
    // This method should be responsible for determining the constraint type of a variable based on its basic row.
    return CONSTRAINT_NON_NEGATIVE; // Assuming all variables are non-negative by default, to be replaced with real logic
}