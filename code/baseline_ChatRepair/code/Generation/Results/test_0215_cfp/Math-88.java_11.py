protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];

    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            coefficients[i] = getEntry(basicRow, getRhsOffset());
            
            // Validating that this row truly represents this variable uniquely
            boolean isValid = true;
            for (int j = 0; j < getTotalNumDecisionVariables(); j++) {
                if (j != getNumObjectiveFunctions() + i && Math.abs(getEntry(basicRow, j)) > 1e-12) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) {
                coefficients[i] = 0;
            }
        } else {
            // If there's no basic row for this variable, its value is zero
            coefficients[i] = 0;
        }
     }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
