protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());

    // Fix: Validate bounds checking
    if (mostNegative < 0 && restrictToNonNegative) {
        mostNegative = 0;
    }

    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        coefficients[i] =
            (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) -
            (restrictToNonNegative ? 0 : mostNegative);

        if (basicRow != null) {
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                if (tableau.getEntry(basicRow, j) == 1) {
                     coefficients[i] = 0;
                }
            }
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}