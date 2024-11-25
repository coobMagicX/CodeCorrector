protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow = null; // Initialize to null since we might change it
    double mostNegative = Double.MAX_VALUE; // Start with a very high value

    // Find the most negative value for the basic row that isn't null
    for (int i = 0; i < getOriginalNumDecisionVariables(); i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        double entry = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
        if (entry < mostNegative && basicRow != null) {
            mostNegative = entry;
        }
    }

    // Iterate over decision variables to calculate the coefficients
    for (int i = 0; i < coefficients.length; i++) {
        double valueOfBasicVariable = Double.MAX_VALUE; // Start with a very high value

        // Find the value of the basic variable that corresponds to the current column
        for (int j = getNumObjectiveFunctions(); j <= getNumObjectiveVariables(); j++) {
            basicRow = getBasicRow(j);
            if (basicRow != null) {
                double entry = getEntry(basicRow, i);
                if (entry == 1) { // This is the current column
                    valueOfBasicVariable = Math.min(valueOfBasicVariable, mostNegative - getEntry(basicRow, getRhsOffset()));
                }
            }
        }

        // Set the coefficient for the decision variable
        coefficients[i] = valueOfBasicVariable;
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}