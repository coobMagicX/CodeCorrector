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
    if (lval != (double) lvalInt) { // Corrected condition to check for non-integer values
      error(FRACTIONAL_BITWISE_OPERAND, left);
      return n;
    }

    int rvalInt = (int) rval;
    if (rval != (double) rvalInt) { // Corrected condition to check for non-integer values
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
        // Correctly handle the unsigned right shift by casting to long and then
        // shifting. This correctly handles negative numbers as JavaScript does.
        result = ((long) lvalInt) >>> rvalInt;
        break; // Cast lvalInt to long before the unsigned right shift operation
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