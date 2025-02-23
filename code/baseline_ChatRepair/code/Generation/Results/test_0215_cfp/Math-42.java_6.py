protected RealPointValuePair getSolution() {
  int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
  Integer negativeVarBasicRow = negativeVarColumn >= 0 ? getBasicRow(negativeVarColumn) : null; // Ensure negativeVarColumn verification uses >= 0
  double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());

  Set<Integer> basicRows = new HashSet<>();
  double[] coefficients = new double[getOriginalNumDecisionVariables()];
  for (int i = 0; i < coefficients.length; i++) {
      int colIndex = columnLabels.indexOf("x" + i);
      if (colIndex < 0) {
        coefficients[i] = 0;
        continue;
      }
      Integer basicRow = getBasicRow(colIndex);
      if (basicRow == null) {
        coefficients[i] = 0; // Handle null basicRow explicitly
        continue;
      }

      if (basicRows.contains(basicRow) || basicRow.equals(negativeVarBasicRow)) { // Check also for the same row as the negative var row
          coefficients[i] = 0;
      } else {
          basicRows.add(basicRow);
          coefficients[i] = getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0 : mostNegative);
      }
  }

  return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
