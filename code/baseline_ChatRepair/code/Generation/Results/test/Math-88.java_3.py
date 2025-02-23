protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());

    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        // Compute coefficient value, taking care if solution should be non-negative
        coefficients[i] =
            (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) -
            (restrictToNonNegative ? 0 : mostNegative);

        // Check if multiple variables can take the given value; we choose the first and set the rest to 0
        if (basicRow != null) {
            // Only check previous columns to ensure we do not overwrite values incorrectly
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                if (tableau.getEntry(basicRow, j) == 1) {
                    coefficients[i] = 0;
                    break; // break as soon as we find a 1, no need to check further
                }
            }
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
