protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow;
    double mostNegative = 0;
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        coefficients[i] = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset()));
        if (basicRow != null) {
            boolean isUnique = true;
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + coefficients.length; j++) {
                if (MathUtils.equals(tableau.getEntry(basicRow, j), 1.0, epsilon) && i != j - getNumObjectiveFunctions()) {
                    isUnique = false;
                    break;
                }
            }
            coefficients[i] = isUnique ? coefficients[i] : 0;
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}