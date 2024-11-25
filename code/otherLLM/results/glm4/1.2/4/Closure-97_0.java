private Node tryFoldShift(Node n, Node left, Node right) {
  if (left.getType() == Token.NUMBER &&
      right.getType() == Token.NUMBER) {

    double result;
    double lval = left.getDouble();
    double rval = right.getDouble();

    // check ranges. We do not want to clip the double to a 32-bit range,
    // as the user likely does not intend that.
    if (!(lval >= Integer.MIN_VALUE && lval <= Integer.MAX_VALUE)) {
      error(BITWISE_OPERAND_OUT_OF_RANGE, left);
      return n;
    }

    // Only the lower 5 bits are used when shifting; don't do anything
    // if the shift amount is outside [0, 32).
    if (!(rval >= 0 && rval < 32)) {
      error(SHIFT_AMOUNT_OUT_OF_BOUNDS, right);
      return n;
    }

    // Convert the numbers to ints, check for integer conversion issues.
    int lvalInt = (int) lval;
    if (lval != (double) lvalInt) { // Corrected to use double cast for comparison
      error(FRACTIONAL_BITWISE_OPERAND, left);
      return n;
    }

    int rvalInt = (int) rval;
    if (rval != (double) rvalInt) {
      error(FRACTIONAL_BITWISE_OPERAND, right);
      return n;
    }

    switch (n.getType()) {
      case Token.LSH:
        result = lvalInt << rvalInt;
        break;
      case Token.RSH:
        result = lvalInt >> rvalInt;
        break;
      case Token.URSH:
        // JavaScript handles zero shifts on signed numbers differently than
        // Java as an int can not represent the unsigned 32-bit number where
        // JavaScript can. So use a long here for calculations.
        result = ((long) lvalInt) >>> rvalInt; // Use (long) cast for correct unsigned right shift
        break;
      default:
        throw new AssertionError("Unknown shift operator: " +
            Node.tokenToName(n.getType()));
    }

    Node newNumber = Node.newNumber(result);
    n.getParent().replaceChild(n, newNumber);
    reportCodeChange();

    return newNumber;
  }

  return n;
}