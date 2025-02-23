protected RealPointValuePair getSolution() {
  int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
  Integer negativeVarBasicRow = negativeVarColumn > 0 ? getBasicRow(negativeVarColumn) : null;
  double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());

  Set<Integer> basicRows = new HashSet<Integer>();
  double[] coefficients = new double[getOriginalNumDecisionVariables()];
  for (int i = 0; i < coefficients.length; i++) {
      int colIndex = columnLabels.indexOf("x" + i);
      if (colIndex < 0) {
        // Ignore the variable, it is not present in this tableau.
        coefficients[i] = 0;
        continue;
      }
      Integer basicRow = getBasicRow(colIndex);

      // Check if the variable should be deduced, no repetitive basic rows
      if (basicRow != null && !basicRows.contains(basicRow)) {
          basicRows.add(basicRow);
          coefficients[i] = getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0 : mostNegative);
      } else {
          // Set to zero if impossible to deduce or already deduced for repetitive row
          coefficients[i] = 0;
      }
  }
  return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
