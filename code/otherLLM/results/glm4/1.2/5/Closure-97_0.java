private Node tryFoldShift(Node n, Node left, Node right) {
  if (left.getType() == Token.NUMBER &&
      right.getType() == Token.NUMBER) {

    double result;
    double lval = left.getDouble();
    double rval = right.getDouble();

    // check ranges. We do not do anything that would clip the double to
    // a 32-bit range, since the user likely does not intend that.
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
    int lvalInt = (int) lval;

    // Check for rounding issues during conversion
    if (lval != (double) lvalInt) {
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
        // Use a long to handle the unsigned right shift for negative numbers
        long lvalLong = (long) lvalInt;
        result = (lvalLong >>> rvalInt);
        break; // Corrected this line
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