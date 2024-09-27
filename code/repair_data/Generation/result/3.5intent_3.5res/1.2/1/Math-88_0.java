protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow = getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        // if multiple variables can take a given value
        // then we choose the first and set the rest equal to 0
        double coefficientValue = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) - (restrictToNonNegative ? 0 : mostNegative);
        if (basicRow != null) {
            boolean foundOne = false;
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + getOriginalNumDecisionVariables(); j++) {
                if (tableau.getEntry(basicRow, j) == 1) {
                    if (!foundOne) {
                        foundOne = true;
                    } else {
                        coefficientValue = 0;
                    }
                }
            }
        }
        coefficients[i] = coefficientValue;
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}