protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            boolean isBasic = true;
            // Verify if the current column represents a basic variable
            for (int j = 0; j < getTotalNumRows(); j++) {
                double entry = tableau.getEntry(j, getNumObjectiveFunctions() + i);
                // Check for the pattern of a basic variable - exactly one 1 in the column and the rest are 0s
                if ((j != basicRow && Math.abs(entry) > 1.0e-12) || (j == basicRow && Math.abs(entry - 1.0) > 1.0e-12)) {
                    isBasic = false;
                    break;
                }
            }
            if (isBasic) {
                coefficients[i] = getEntry(basicRow, getRhsOffset());
            } else {
                coefficients[i] = 0;
            }
        } else {
            coefficients[i] = 0;
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
