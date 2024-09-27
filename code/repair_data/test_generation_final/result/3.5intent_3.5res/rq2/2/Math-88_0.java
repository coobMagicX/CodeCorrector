protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow = getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        coefficients[i] = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) - (restrictToNonNegative ? 0 : mostNegative);
        if (basicRow != null) {
            boolean hasNonZeroCoefficient = false;
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                if (tableau.getEntry(basicRow, j) != 0) {
                    hasNonZeroCoefficient = true;
                    break;
                }
            }
            if (!hasNonZeroCoefficient) {
                coefficients[i] = 0;
            }
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}