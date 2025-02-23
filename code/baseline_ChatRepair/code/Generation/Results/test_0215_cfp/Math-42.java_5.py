protected RealPointValuePair getSolution() {
    int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
    Integer negativeVarBasicRow = negativeVarColumn > -1 ? getBasicRow(negativeVarColumn) : null;
    double mostNegative = negativeVarBasicRow == null ? 0.0 : getEntry(negativeVarBasicRow, getRhsOffset());

    Map<Integer, Double> basicRowToValue = new HashMap<Integer, Double>();
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    for (int i = 0; i < coefficients.length; i++) {
        int colIndex = columnLabels.indexOf("x" + i);
        if (colIndex == -1) {
            coefficients[i] = 0.0; // Ensure variables not in the problem have coefficient 0
        } else {
            Integer basicRow = getBasicRow(colIndex);
            if (basicRow == null || basicRowToValue.containsKey(basicRow)) {
                // If no basic row or if already used, coefficient defaults or retains zero
                coefficients[i] = 0.0;
            } else {
                // Calculate the value for this variable
                double coefficientValue = getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0.0 : mostNegative);
                coefficients[i] = coefficientValue;
                basicRowToValue.put(basicRow, coefficientValue); // Map basic row to its computed value
            }
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
