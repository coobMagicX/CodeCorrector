protected RealPointValuePair getSolution() {
  int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
  Integer negativeVarBasicRow = negativeVarColumn > 0 ? getBasicRow(negativeVarColumn) : null;
  double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());

  Set<Integer> basicRows = new HashSet<Integer>();
  double[] coefficients = new double[getOriginalNumDecisionVariables()];
  for (int i = 0; i < coefficients.length; i++) {
      int colIndex = columnLabels.indexOf("x" + i);
      if (colIndex < 0) {
        coefficients[i] = 0;
        continue;
      }
      Integer basicRow = getBasicRow(colIndex);

      // Check if the variable is already accounted for in the basic solution
      if (basicRows.contains(basicRow)) {
          // If it's an unconstrained variable in the objective function, set coefficient to zero
          coefficients[i] = 0;
      } else {
          basicRows.add(basicRow);

          // Calculate the coefficient considering the 'restrictToNonNegative' flag
          double entryValue = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
          coefficients[i] = restrictToNonNegative ? entryValue : entryValue - mostNegative;
      }
  }
  return new RealPointValuePair(coefficients, f.getValue(coefficients));
}