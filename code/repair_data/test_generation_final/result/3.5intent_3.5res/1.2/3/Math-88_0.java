protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        coefficients[i] =
            (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) -
            (restrictToNonNegative ? 0 : mostNegative);
        if (basicRow != null) {
            boolean foundOne = false;
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + getOriginalNumDecisionVariables(); j++) {
                if (tableau.getEntry(basicRow, j) == 1) {
                    if (!foundOne) {
                        coefficients[i] = getEntry(basicRow, getRhsOffset());
                        foundOne = true;
                    } else {
                        coefficients[i] = 0;
                    }
                }
            }
        }
    }
    return new RealPointValuePair(coefficients, calculateObjectiveValue(coefficients));
}