protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    for (int i = 0; i < coefficients.length; i++) {
        int colIndex = getNumObjectiveFunctions() + i;
        Integer basicRow = getBasicRow(colIndex);
        if (basicRow != null) {
            coefficients[i] = getEntry(basicRow, getRhsOffset()); // Extract coefficient directly from the tableau
        } else {
            coefficients[i] = 0; // Set non-basic variables coefficients to zero
        }
    }

    // Post-processing: Ensuring non-negativity if the model requires.
    if (restrictToNonNegative) {
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = Math.max(0, coefficients[i]); // Clamping to zero if negative
        }
    }

    double value = f.getValue(coefficients);
    return new RealPointValuePair(coefficients, value);
}
