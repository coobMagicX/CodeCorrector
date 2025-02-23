protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];

    // Iterate over each decision variable
    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        // Set coefficient based on whether a basicRow exists and corresponding entry
        if (basicRow != null) {
            boolean isValidBasicVariable = true;
            double entryInBasicRow = tableau.getEntry(basicRow, getNumObjectiveFunctions() + i);
            
            // Check if this entry is the only non-zero in its column, indicating a basic variable
            for (int row = 0; row < numberOfConstraints + numberOfObjectiveFunctions; row++) {  // assuming numberOfConstraints and numberOfObjectiveFunctions are defined
                if (row != basicRow && Math.abs(tableau.getEntry(row, getNumObjectiveFunctions() + i)) > 1.0e-12) {
                    isValidBasicVariable = false;
                    break;
                }
            }

            if (isValidBasicVariable && Math.abs(entryInBasicRow - 1.0) < 1.0e-12) {
                coefficients[i] = getEntry(basicRow, getRhsOffset());  // Take RHS value if valid basic variable
            } else {
                coefficients[i] = 0;
            }
        } else {
            coefficients[i] = 0;
        }
    }
    
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
