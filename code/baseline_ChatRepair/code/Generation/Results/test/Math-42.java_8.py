protected RealPointValuePair getSolution() {
  int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
  Integer negativeVarBasicRow = negativeVarColumn >= 0 ? getBasicRow(negativeVarColumn) : null;
  double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());

  Set<Integer> basicRows = new HashSet<Integer>(); // for Java 1.5 compatibility
  double[] coefficients = new double[getOriginalNumDecisionVariables()];

  for (int i = 0; i < coefficients.length; i++) {
    int colIndex = columnLabels.indexOf("x" + i);
    if (colIndex < 0) {
      coefficients[i] = 0; // Handle columns not present
      continue;
    }
    Integer basicRow = getBasicRow(colIndex);
    if (basicRow == null || basicRows.contains(basicRow)) {
      coefficients[i] = 0; // Set coefficient to 0 if it's not a basic variable or already counted
    } else {
      basicRows.add(basicRow);
      coefficients[i] = getEntry(basicRow, getRhsOffset());
      if (!restrictToNonNegative) {
        coefficients[i] -= mostNegative; // adjust by mostNegative only if not restricted to non-negative values
      }
      if (coefficients[i] < 0) { // Ensure non-negativity if required
        coefficients[i] = 0;
      }
    }
  }

  return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
