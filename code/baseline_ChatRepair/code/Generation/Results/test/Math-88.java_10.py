protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    double mostNegative = Double.MAX_VALUE;

    for (int i = 0; i < getOriginalNumDecisionVariables(); i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            mostNegative = Math.min(mostNegative, getEntry(basicRow, getRhsOffset()));
        }
    }

    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            boolean onlyBasicNonZero = true;
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + getOriginalNumDecisionVariables(); j++) {
                if (j != getNumObjectiveFunctions() + i && tableau.getEntry(basicRow, j) != 0) {
                    onlyBasicNonZero = false;
                    break;
                }
            }
            if (onlyBasicNonZero) {
                coefficients[i] = getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0 : mostNegative);
            } else {
                coefficients[i] = 0;
            }
        } else {
            coefficients[i] = 0;
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
