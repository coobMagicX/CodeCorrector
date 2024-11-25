private Node tryFoldShift(Node n, Node left, Node right) {
  if (left.getType() == Token.NUMBER &&
      right.getType() == Token.NUMBER) {

    double result;
    double lval = left.getDouble();
    double rval = right.getDouble();

    // check ranges. We do not clip the double to a 32-bit range because
    // the user likely does not intend that.
    if (!(lval >= Integer.MIN_VALUE && lval <= Integer.MAX_VALUE)) {
      error(BITWISE_OPERAND_OUT_OF_RANGE, left);
      return n;
    }

    // only the lower 5 bits are used when shifting, so don't do anything
    // if the shift amount is outside [0,32)
    if (!(rval >= 0 && rval < 32)) {
      error(SHIFT_AMOUNT_OUT_OF_BOUNDS, right);
      return n;
    }

    // Convert the numbers to ints
    int lvalInt = (int) Math.round(lval); // Ensure that rounding is done on double value
    if (lval != lvalInt && lval > Integer.MAX_VALUE || lval < Integer.MIN_VALUE) {
      error(FRACTIONAL_BITWISE_OPERAND, left);
      return n;
    }

    int rvalInt = (int) Math.round(rval); // Ensure that rounding is done on double value
    if (rval != rvalInt && rval > 31 || rval < 0) { // Shift amount should be within [0, 31]
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
        // JavaScript can, so use a long here to handle the shift correctly.
        long lvalLong = Math.round(lval); // Use long for the operation
        result = lvalLong >>> rvalInt;
        break; // Use unsigned right shift on long to ensure correct behavior in JavaScript-like environment
      default:
        throw new AssertionError("Unknown shift operator: " +
            Node.tokenToName(n.getType()));
    }

    if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
      error(BITWISE_OPERAND_OUT_OF_RANGE, left);
      return n;
    }

    Node newNumber = Node.newNumber(result);
    n.getParent().replaceChild(n, newNumber);
    reportCodeChange();

    return newNumber;
  }

  return n;
}