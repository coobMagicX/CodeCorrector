protected RealPointValuePair getSolution() {
  int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
  Integer negativeVarBasicRow = negativeVarColumn > 0 ? getBasicRow(negativeVarColumn) : null;
  double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());

  Set<Integer> encounteredBasicRows = new HashSet<>();
  double[] coefficients = new double[getOriginalNumDecisionVariables()];
  for (int i = 0; i < coefficients.length; i++) {
      int colIndex = columnLabels.indexOf("x" + i);
      if (colIndex < 0) {
        coefficients[i] = 0;
        continue;
      }
      Integer basicRow = getBasicRow(colIndex);
      coefficients[i] = 0; // Default to 0
      if (basicRow != null) {
        if (!encounteredBasicRows.contains(basicRow)) {
          // This basic row has not been assigned yet
          encounteredBasicRows.add(basicRow);
          coefficients[i] = getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0 : mostNegative);
        }
        // Note: if basicRow is already used, coefficient[i] remains 0, as intended.
      }
  }
  return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
